package com.example.calorietracker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NutritionApi {
    @GET("natural/nutrients")
    fun getCalorieInfo(
        @Query("query") itemName: String,
        @Query("x-app-id") appId: String,
        @Query("x-app-key") appKey: String
    ): Call<CalorieResponse>
}