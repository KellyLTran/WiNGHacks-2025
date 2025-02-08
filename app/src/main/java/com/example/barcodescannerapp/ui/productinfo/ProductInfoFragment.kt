package com.example.barcodescannerapp.ui.productinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.barcodescannerapp.databinding.FragmentHomeBinding
import com.example.barcodescannerapp.databinding.FragmentProductinfoBinding
import com.example.barcodescannerapp.ui.home.HomeViewModel
import com.example.barcodescannerapp.ExcelReader

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

        val excelReader = ExcelReader(requireContext())
        val brandList = excelReader.getBrandData()

        // Build a string to display brands and their attributes
        val displayText = if (brandList.isNotEmpty()) {
            brandList.joinToString("\n\n") { brand ->
                """
            Brand: ${brand.name}
            Fully Vegan: ${if (brand.allVegan) "Yes" else "No"}
            Partially Vegan: ${if (brand.partialVegan) "Yes" else "No"}
            Black Owned: ${if (brand.blackOwned) "Yes" else "No"}
            """.trimIndent()
            }
        } else {
            "No brands were found."
        }
        binding.textProductInfo.text = displayText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}