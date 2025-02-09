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

        // Display only the selected brand's information
        val displayText = if (selectedBrandInfo != null) {
            """
            Fully Vegan: ${if (selectedBrandInfo.allVegan) "Yes" else "No"}
            Partially Vegan: ${if (selectedBrandInfo.partialVegan) "Yes" else "No"}
            Black Owned: ${if (selectedBrandInfo.blackOwned) "Yes" else "No"}
            """.trimIndent()
        } else {
            "Brand Data Not Found"
        }
        binding.textProductInfo.text = displayText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}