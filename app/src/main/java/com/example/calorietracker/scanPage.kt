package com.example.calorietracker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.calorietracker.CalorieResponse
import com.example.calorietracker.NutritionApi
import com.example.calorietracker.R
import com.google.android.datatransport.runtime.BuildConfig
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class scanPage : Fragment() {
    companion object {
        const val REQUEST_GALLERY_IMAGE = 2
    }

    private lateinit var imageViewPreview: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var textViewCalorieInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan_page, container, false)

        imageViewPreview = view.findViewById(R.id.imageViewPreview)
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage)
        textViewCalorieInfo = view.findViewById(R.id.textViewCalorieInfo)

        buttonSelectImage.setOnClickListener {
            openGallery()
        }

        return view
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_GALLERY_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY_IMAGE -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                        handleImage(bitmap)
                    }
                }
            }
        }
    }

    private fun handleImage(bitmap: Bitmap) {
        imageViewPreview.setImageBitmap(bitmap)

        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val detectedText = visionText.text
                getCalorieInfo(detectedText)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun getCalorieInfo(foodItem: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://trackapi.nutritionix.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NutritionApi::class.java)
        val call = api.getCalorieInfo(foodItem, BuildConfig.NUTRITION_APP_ID, BuildConfig.NUTRITION_APP_KEY)

        call.enqueue(object : Callback<CalorieResponse> {
            override fun onResponse(call: Call<CalorieResponse>, response: Response<CalorieResponse>) {
                if (response.isSuccessful) {
                    val calorieInfo = response.body()
                    displayCalorieInfo(calorieInfo)
                } else {
                    textViewCalorieInfo.text = "Failed to retrieve calorie information."
                }
            }

            override fun onFailure(call: Call<CalorieResponse>, t: Throwable) {
                t.printStackTrace()
                textViewCalorieInfo.text = "Error: ${t.message}"
            }
        })
    }

    private fun displayCalorieInfo(calorieInfo: CalorieResponse?) {
        if (calorieInfo != null && calorieInfo.foods.isNotEmpty()) {
            val food = calorieInfo.foods[0]
            textViewCalorieInfo.text = "Food: ${food.foodName}\nCalories: ${food.calories}"
        } else {
            textViewCalorieInfo.text = "Calorie information not found."
        }
    }
}
