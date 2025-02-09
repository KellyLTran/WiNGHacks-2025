package com.example.barcodescannerapp.ui.productinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.barcodescannerapp.databinding.FragmentHomeBinding
import com.example.barcodescannerapp.databinding.FragmentProductinfoBinding
import com.example.barcodescannerapp.ui.home.HomeViewModel
import androidx.navigation.fragment.findNavController
import com.example.barcodescannerapp.ExcelReader
import android.util.Log
import android.text.style.ImageSpan
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import androidx.core.content.ContextCompat
import com.example.barcodescannerapp.R

class ProductInfoFragment : Fragment() {

    private var _binding: FragmentProductinfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductinfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Get the selected brand name that the user clicked
        val selectedBrand = arguments?.getString("selectedItem") ?: "Unknown Brand"
        val sourceFragment = arguments?.getString("sourceFragment") ?: "home"
        Log.d("ProductInfoFragment", "Received selected brand: $selectedBrand from $sourceFragment")

        val excelReader = ExcelReader(requireContext())
        val brandList = excelReader.getBrandData()

        // Find the specific brand by name
        val selectedBrandInfo = brandList.find { it.name == selectedBrand }

        if (selectedBrandInfo != null) {

            // Get the images from the drawable directory
            val yesDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.yes_icon)
            val noDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.no_icon)

            // Can resize the images with the boundary numbers
            fun getImageSpan(drawable: Drawable?): ImageSpan {
                drawable?.setBounds(0, 0, 40, 40)
                return ImageSpan(drawable!!, ImageSpan.ALIGN_BASELINE)
            }

            // Create a SpannableString with labels for the images
            val text = SpannableString("Fully Vegan:  \nPartially Vegan:  \nBlack Owned:  ")

            text.setSpan(getImageSpan(if (selectedBrandInfo.allVegan) yesDrawable else noDrawable), 13, 14, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            text.setSpan(getImageSpan(if (selectedBrandInfo.partialVegan) yesDrawable else noDrawable), 31, 32, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            text.setSpan(getImageSpan(if (selectedBrandInfo.blackOwned) yesDrawable else noDrawable), 47, 48, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

            binding.textProductInfo.text = text
        } else {
            binding.textProductInfo.text = "Brand Data Not Found"
        }
        binding.textProductInfo.text = displayText

        binding.backButton.setOnClickListener {
            if (sourceFragment == "home") {
                findNavController().navigate(R.id.navigation_home)
            } else {
                findNavController().navigate(R.id.navigation_dashboard)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}