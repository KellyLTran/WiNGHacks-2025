package com.example.barcodescannerapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.barcodescannerapp.ExcelReader
import com.example.barcodescannerapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Find TextView inside fragment_dashboard.xml and display the excel data
        val excelDataTextView: TextView = binding.excelDataTextView
        displayExcelData(excelDataTextView)

        return root
    }

    // Read the Excel file and display it in the TextView
    private fun displayExcelData(textView: TextView) {

        // Create an instance of the ExcelReader class then call the readExcelFile function on it
        val excelReader = ExcelReader(requireContext())
        val excelData = excelReader.readExcelFile()

        // Convert each row into a readable string format
        if (excelData.isNotEmpty()) {
            val displayText = excelData.joinToString("\n\n") { row ->
                "• " + row.joinToString(" | ")
            }
            // Display the formatted text inside the TextView
            textView.text = displayText

            /* Testing only first 5 rows:
            val testData = excelData.take(5)
            val displayText = testData.joinToString("\n") { row -> row.joinToString(", ") }
            textView.text = displayText */
        } else {
            textView.text = "Error: No data was found."
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
