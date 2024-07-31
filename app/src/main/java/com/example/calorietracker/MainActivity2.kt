package com.example.calorietracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Get the username from the intent extras
        val userName = intent.getStringExtra("name") ?: ""

        // Create the HomePage fragment with the arguments
        val homeFragment = homePage().apply {
            arguments = Bundle().apply {
                putString("name", userName)
            }
        }

        // Initialize the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Load the HomePage fragment by default
        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, homeFragment)
            .commit()

        // Set the bottom navigation item selected listener
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    loadFragment(homePage().apply {
                        arguments = Bundle().apply {
                            putString("name", userName)
                        }
                    })
                }

                R.id.bottom_scan -> {
                    loadFragment(scanPage())
                }

                R.id.bottom_profile -> {
                    loadFragment(profilePage().apply {
                        arguments = Bundle().apply {
                            putString("name", userName)
                        }
                    })
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
