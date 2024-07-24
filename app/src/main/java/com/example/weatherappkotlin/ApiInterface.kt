package com.example.weatherappkotlin

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("weather")
    fun getWeatherData(
        @retrofit2.http.Query("q") city: String,
        @retrofit2.http.Query("appid") apiKey: String,
        @retrofit2.http.Query("units") units: String
    ): Call<WeatherItem>
}