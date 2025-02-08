package com.example.barcodescannerapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.barcodescannerapp.databinding.ActivityMainBinding
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // creating variables for listview
    lateinit var productLV: ListView

    // creating array adapter for listview
    lateinit var listAdapter: ArrayAdapter<String>

    // creating array list for listview
    lateinit var productList: ArrayList<String>;

    // creating variable for searchview
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        // initializing variables of list view with their ids.
        productLV = findViewById(R.id.idLVProducts)
        searchView = findViewById(R.id.idSV)

        productList = ArrayList()
        productList.add("La Roche Posay")
        productList.add("KKW Beauty")
        productList.add("ELF")
        productList.add("Revlon")
        productList.add("Lo'Real")

        // initializing list adapter and setting layout
        // for each list view item and adding array list to it.
        listAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            productList
        )

        // on below line setting list
        // adapter to our list view.
        productLV.adapter = listAdapter

        // on below line we are adding on query
        // listener for our search view.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (productList.contains(query)) {
                    listAdapter.filter.filter(query)
                } else {
                    Toast.makeText(this@MainActivity, "No Product Found..", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })
        
        //val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)
    }
}