package com.example.barcodescannerapp.ui.home

import android.Manifest
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.barcodescannerapp.BarcodeInfo
import com.example.barcodescannerapp.BoundingBoxView
import com.example.barcodescannerapp.databinding.FragmentHomeBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.emptyLongSet
import com.example.barcodescannerapp.BarcodeInfo
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var previewView: PreviewView
    private lateinit var resultTextView: TextView
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScanner: BarcodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        previewView = binding.previewView
        resultTextView = binding.resultTextView

        barcodeScanner = BarcodeScanning.getClient()
        cameraExecutor = Executors.newSingleThreadExecutor()

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                resultTextView.text = "Camera permission is required."
            }
        }

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        val height = Resources.getSystem().displayMetrics.heightPixels
        val width = Resources.getSystem().displayMetrics.widthPixels

        binding.boundingBoxView.setCoordinates(
            (width * 0.05).toInt(),
            (height * 0.4).toInt(),
            (width * 0.95).toInt(),
            (height * 0.6).toInt()
        )
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val height = Resources.getSystem().displayMetrics.heightPixels
            val width = Resources.getSystem().displayMetrics.widthPixels

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    private fun inBoundingBox(imageBoundingBox: Rect, imageProxy: ImageProxy): Boolean {
        val mediaImage = imageProxy.image ?: return false

        val imageWidth = mediaImage.width  // 640
        val imageHeight = mediaImage.height // 480

        val previewWidth = previewView.width  // 1080
        val previewHeight = previewView.height // 1874

        val isImageRotated = imageProxy.imageInfo.rotationDegrees == 90 || imageProxy.imageInfo.rotationDegrees == 270

        val adjustedImageWidth = if (isImageRotated) imageHeight else imageWidth
        val adjustedImageHeight = if (isImageRotated) imageWidth else imageHeight

        val scaleX = previewWidth.toFloat() / adjustedImageWidth // 1080 / 640
        val scaleY = previewHeight.toFloat() / adjustedImageHeight // 1874 / 480

        val scale = minOf(scaleX, scaleY)

        val offsetX = (previewWidth - (adjustedImageWidth * scale)) / 2
        val offsetY = (previewHeight - (adjustedImageHeight * scale)) / 2

        val transformedBoundingBox = Rect(
            (imageBoundingBox.left * scale + offsetX).toInt(),
            (imageBoundingBox.top * scale + offsetY).toInt(),
            (imageBoundingBox.right * scale + offsetX).toInt(),
            (imageBoundingBox.bottom * scale + offsetY).toInt()
        )

        val screenBoundingBox = Rect(
            (previewWidth * 0.05).toInt(),
            (previewHeight * 0.4).toInt(),
            (previewWidth * 0.95).toInt(),
            (previewHeight * 0.6).toInt()
        )

        val inside = screenBoundingBox.contains(transformedBoundingBox)

        return inside
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                   for (barcode in barcodes) {
                        val boundingBox = barcode.boundingBox

                        if (boundingBox != null && inBoundingBox(boundingBox, imageProxy)) {
                            handleBarcode(barcode)
                        }
                    }
                }
                .addOnFailureListener {
                    resultTextView.text = "Failed to scan barcode"
                    Log.e("BarcodeScanner", "Barcode scanning failed", it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun handleBarcode(barcode: Barcode) {
        val scannedText = barcode.rawValue ?: "No valid barcode found"

        Log.d("BarcodeScanner", "✅ Scanned barcode: $scannedText") // Debugging log

        CoroutineScope(Dispatchers.Main).launch {
            val barcodeInformation: BarcodeInfo.RootObject? = BarcodeInfo.parseData(scannedText)

            if (barcodeInformation != null) {
                val item = barcodeInformation.products[0].brand
                val action = item?.let {
                    HomeFragmentDirections.actionNavigationHomeToNavigationProductinfo(
                        it
                    )
                }
                if (action != null) {
                    findNavController().navigate(action)
                }

                // Debugging
                // Log.d("BarcodeScanner", "Brand: ${barcodeInformation.products[0].brand}")
                // Log.d("BarcodeScanner", "Title: ${barcodeInformation.products[0].title}")
            }
            else {
                Log.d("BarcodeScanner", "Failed to fetch barcode information")
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
