package com.example.barcodescannerapp

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BarcodeInfo {
    @JvmStatic
    suspend fun parseData(scannedBarcode: String): RootObject? {
        return withContext(Dispatchers.IO) {
            var barcodeInfo: RootObject? = null
            try {
                val apiKey = "ki8b1leowor4n1qpk6f3e4opwygvos"
                val url =
                    URL("https://api.barcodelookup.com/v3/products?barcode=$scannedBarcode&formatted=y&key=$apiKey")
                val br = BufferedReader(InputStreamReader(url.openStream()))
                var str: String? = ""
                var data: String? = ""
                while (null != (br.readLine().also { str = it })) {
                    data += str
                }

                val g = Gson()

                val value: RootObject = g.fromJson<RootObject>(data, RootObject::class.java)
                barcodeInfo = value

                val barcode = value.products[0].barcode_number
                Log.d("Barcode Number: ", barcode.toString())

                val name = value.products[0].title
                Log.d("Title: ", name.toString())

                Log.d("Entire Response:", data.toString())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            barcodeInfo
        }
    }

    class Store {
        var name: String? = null
        var price: String? = null
        var link: String? = null
        var currency: String? = null
        var currency_symbol: String? = null
    }

    class Review {
        var name: String? = null
        var rating: String? = null
        var title: String? = null
        var review: String? = null
        var date: String? = null
    }

    class Product {
        var barcode_number: String? = null
        var barcode_formats: String? = null
        var mpn: String? = null
        var model: String? = null
        var asin: String? = null
        var title: String? = null
        var category: String? = null
        var manufacturer: String? = null
        var brand: String? = null
        lateinit var contributors: Array<Any>
        var age_group: String? = null
        var ingredients: String? = null
        var nutrition_facts: String? = null
        var color: String? = null
        var format: String? = null
        var multipack: String? = null
        var size: String? = null
        var length: String? = null
        var width: String? = null
        var height: String? = null
        var weight: String? = null
        var release_date: String? = null
        var description: String? = null
        lateinit var features: Array<String>
        lateinit var images: Array<String>
        lateinit var stores: Array<Store>
        lateinit var reviews: Array<Review>
    }

    class RootObject {
        lateinit var products: Array<Product>
    }
}