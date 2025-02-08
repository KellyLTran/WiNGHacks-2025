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

    fun readBrandNames(): List<String> {
        val brandNames = mutableListOf<String>()

        try {
            val inputStream: InputStream = context.assets.open("CF_Products_Dataset.xlsx")
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)

            // Start from row index 1 to skip the column titles
            for (i in 1..sheet.lastRowNum) {
                val row = sheet.getRow(i)
                if (row != null) {
                    // From each row, get specifically the data within the second column at index 1
                    val cell = row.getCell(1)
                    if (cell != null) {
                        brandNames.add(cell.toString().trim())
                    }
                }
            }
            workbook.close()

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ExcelReader", "Error reading brand names: ${e.message}")
        }
        return brandNames
    }

    // Function to get column headers and their associated TRUE/FALSE values
    fun getBrandData(): Map<String, Map<String, Boolean>> {

        // Organize the data as { Brand -> { Header -> TRUE/FALSE } }
        val brandData = mutableMapOf<String, MutableMap<String, Boolean>>()

        try {
            val inputStream: InputStream = context.assets.open("CF_Products_Dataset.xlsx")
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)

            val headerRow = sheet.getRow(0)
            val headers = mutableListOf<String>()

            // Get the specific header titles from columns 3-6
            if (headerRow != null) {
                for (cellIndex in 2..5) {
                    val headerCell = headerRow.getCell(cellIndex)
                    if (headerCell != null) {
                        val headerValue = headerCell.stringCellValue.trim()
                        headers.add(headerValue)
                    } else {
                        headers.add("Unknown")
                    }
                }
            }
            val brandNames = readBrandNames()

            // Loop through each row and match each brand with its TRUE/FALSE values
            for ((index, row) in sheet.drop(1).withIndex()) {
                if (index >= brandNames.size) break
                val brandName = brandNames[index]
                val dataValues = mutableMapOf<String, Boolean>()

                // Extract TRUE/FALSE values from columns 3-6
                for (cellIndex in 2..5) {
                    val cell = row.getCell(cellIndex)
                    if (cell != null) {
                        val cellValue = cell.stringCellValue.trim().uppercase()
                        dataValues[headers[cellIndex - 2]] = (cellValue == "TRUE")
                    } else {
                        dataValues[headers[cellIndex - 2]] = false
                    }
                }
                brandData[brandName] = dataValues
            }
            workbook.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ExcelReader", "Error reading brand data: ${e.message}")
        }
        return brandData
    }
}

