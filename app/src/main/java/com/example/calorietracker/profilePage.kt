package com.example.calorietracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class profilePage : Fragment() {
    private lateinit var weight: EditText
    private lateinit var height: EditText
    private lateinit var name: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        // Initialize views
        name = view.findViewById(R.id.profile_name)
        height = view.findViewById(R.id.editTextNumberDecimal2)
        weight = view.findViewById(R.id.editTextNumberDecimal)

        // Get the username from the arguments
        val userName = arguments?.getString("name") ?: ""
        name.text = userName

        return view
    }

    // Method to calculate BMI
    private fun calculateBMI(): String {
        val weightValue = weight.text.toString().toDoubleOrNull() ?: 0.0
        val heightValue = height.text.toString().toDoubleOrNull() ?: 1.0

        val bmi = weightValue / (heightValue * heightValue)
        return "Your BMI is %.2f".format(bmi)
    }

    private fun showBMI() {
        val bmiText = calculateBMI()
        // Assuming you have a TextView to display the BMI result
        val bmiTextView: TextView? = view?.findViewById(R.id.textView10)
        bmiTextView?.text = bmiText
    }
}
