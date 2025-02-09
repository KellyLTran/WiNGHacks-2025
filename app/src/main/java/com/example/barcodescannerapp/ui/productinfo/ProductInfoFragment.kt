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

        // Get and display the specific brand that the user clicked
        val selectedBrand = arguments?.getString("selectedItem") ?: "Unknown Brand"
        // Testing: Log.d("ProductInfoFragment", "Received selected brand: $selectedBrand")
        binding.titleText2.text = selectedBrand

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
                drawable?.setBounds(0, 0, 90, 90)
                return ImageSpan(drawable!!, ImageSpan.ALIGN_CENTER)
            }

            // Create a SpannableString with labels for the images
            val text = SpannableString(
                "Fully Vegan:  X\n" +
                        "Partially Vegan:  X\n" +
                        "Black Owned:  X"
            )

            // Find the positions of "X" dynamically
            val veganIndex = text.indexOf("X")
            val partialVeganIndex = text.indexOf("X", veganIndex + 1)
            val blackOwnedIndex = text.indexOf("X", partialVeganIndex + 1)

            // Apply ImageSpans after each label
            text.setSpan(getImageSpan(if (selectedBrandInfo.allVegan) yesDrawable else noDrawable),
                veganIndex, veganIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

            text.setSpan(getImageSpan(if (selectedBrandInfo.partialVegan) yesDrawable else noDrawable),
                partialVeganIndex, partialVeganIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

            text.setSpan(getImageSpan(if (selectedBrandInfo.blackOwned) yesDrawable else noDrawable),
                blackOwnedIndex, blackOwnedIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

            binding.textProductInfo.text = text
        } else {
            binding.textProductInfo.text = "Brand Data Not Found"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}