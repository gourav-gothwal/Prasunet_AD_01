package com.example.calorietracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class homePage : Fragment() {

    private lateinit var addCaloriesButton: Button
    private lateinit var addWaterButton: Button
    private lateinit var totalCaloriesTextView: TextView
    private lateinit var totalWaterTextView: TextView
    private lateinit var calorieProgressBar: ProgressBar
    private lateinit var waterProgressBar: ProgressBar
    private lateinit var name: TextView

    private var totalCalories = 0
    private var totalWater = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        addCaloriesButton = view.findViewById(R.id.addCaloriesButton)
        addWaterButton = view.findViewById(R.id.addWaterButton)
        totalCaloriesTextView = view.findViewById(R.id.total_calories_text_view)
        totalWaterTextView = view.findViewById(R.id.total_water_text_view)
        calorieProgressBar = view.findViewById(R.id.calories_progressBar)
        waterProgressBar = view.findViewById(R.id.water_progressBar)
        name = view.findViewById(R.id.textView8)

        val userName = arguments?.getString("name") ?: ""
        name.text = "$userName"

        calorieProgressBar.max = 2000
        waterProgressBar.max = 3000

        addCaloriesButton.setOnClickListener {
            showCalorieAlert(requireContext())
        }

        addWaterButton.setOnClickListener {
            showWaterAlert(requireContext())
        }
        return view
    }

    private fun showCalorieAlert(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Calories")
        builder.setMessage("Enter value")
        val input = EditText(context)
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, _ ->
            val value = input.text.toString()
            if (value.isNotEmpty()) {
                val calories = value.toIntOrNull() ?: 0
                updateTotalCalories(calories)
            } else {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun showWaterAlert(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Water")
        builder.setMessage("Enter value (ml)")
        val input = EditText(context)
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, _ ->
            val value = input.text.toString()
            if (value.isNotEmpty()) {
                val water = value.toIntOrNull() ?: 0
                updateTotalWater(water)
            } else {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun updateTotalCalories(caloriesToAdd: Int) {
        totalCalories += caloriesToAdd
        totalCaloriesTextView.text = totalCalories.toString()
        calorieProgressBar.progress = totalCalories
    }

    private fun updateTotalWater(waterToAdd: Int) {
        totalWater += waterToAdd
        totalWaterTextView.text = totalWater.toString()
        waterProgressBar.progress = totalWater
    }
}
