package com.example.barcodescannerapp
import android.content.Context
import android.util.Log
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

// Read the Excel dataset file stored in the assets folder then return the list of rows
class ExcelReader(private val context: Context) {

    fun readExcelFile(): List<List<String>> {
        val rowDataList = mutableListOf<List<String>>()

        try {
            // Open the Excel file and create an XSSFWorkbook instance to read the file
            val inputStream: InputStream = context.assets.open("CF_Products_Dataset.xlsx")
            val workbook = XSSFWorkbook(inputStream)

            // Loop through each row in the first sheet
            val sheet = workbook.getSheetAt(0)
            for (i in 0..sheet.lastRowNum) {
                val row = sheet.getRow(i)
                val rowData = mutableListOf<String>()

                // Loop through each cell and convert the data into a string then add it to the list
                if (row != null) {
                    for (cell in row) {
                        rowData.add(cell.toString())
                    }
                    rowDataList.add(rowData)
                }
            }
            workbook.close()

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ExcelReader", "Error reading Excel file: ${e.message}")
        }
        return rowDataList
    }
}
