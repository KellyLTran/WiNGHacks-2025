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

class ProductInfoFragment : Fragment() {

    private var _binding: FragmentProductinfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // ActionBar with back button
//        (activity as? AppCompatActivity)?.supportActionBar?.apply {
//            show() // Show the ActionBar
//            setDisplayHomeAsUpEnabled(true)
//            title = "Product Info"
//        }

        // Handle back button press
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val productinfoViewModel =
            ViewModelProvider(this).get(ProductInfoViewModel::class.java)

        _binding = FragmentProductinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    {
        }
    }
}