package com.example.weatherappkotlin

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WeatherAppPreferences", Context.MODE_PRIVATE)

    companion object {
        const val LAST_CITY = "last_city"
    }

    fun saveLastCity(value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(LAST_CITY, value)
        editor.apply()
    }

    fun getLastCity(): String? {
        return sharedPreferences.getString(LAST_CITY, null)
    }
}