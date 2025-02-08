package com.example.barcodescannerapp
import android.content.Context
import android.util.Log
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import org.apache.poi.ss.usermodel.Cell

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

    fun getBooleanValue(cell: Cell?): Boolean {
        if (cell != null) {
            val cellValue = cell.toString().trim().uppercase()
            if (cellValue == "TRUE") {
                return true
            }
        }
        return false
    }

    class BrandInfo(
        val name: String,
        val allVegan: Boolean,
        val partialVegan: Boolean,
        val blackOwned: Boolean
    )

    fun getBrandData(): ArrayList<BrandInfo> {
        val brandList = ArrayList<BrandInfo>()

        try {
            val inputStream: InputStream = context.assets.open("CF_Products_Dataset.xlsx")
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)

            // Loop through each row starting from index 1 to skip the headers
            for (i in 1..sheet.lastRowNum) {
                val row = sheet.getRow(i)
                if (row != null) {
                    val brandNameCell = row.getCell(1)
                    val allVeganCell = row.getCell(2)
                    val partialVeganCell = row.getCell(3)
                    val blackOwnedCell = row.getCell(5)

                    // Get the boolean values for each of the column headers
                    if (brandNameCell != null) {
                        val brandName = brandNameCell.stringCellValue.trim()
                        val allVegan = getBooleanValue(allVeganCell)
                        val partialVegan = getBooleanValue(partialVeganCell)
                        val blackOwned = getBooleanValue(blackOwnedCell)
                        brandList.add(BrandInfo(brandName, allVegan, partialVegan, blackOwned))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ExcelReader", "Error reading brand data: ${e.message}")
        }
        return brandList
    }
}
