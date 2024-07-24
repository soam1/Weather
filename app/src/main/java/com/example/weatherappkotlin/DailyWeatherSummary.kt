package com.example.weatherappkotlin

data class DailyWeatherSummary(
    val date: String,
    val averageTemp: Double,
    val maxTemp: Double,
    val minTemp: Double,
    val dominantCondition: String
)