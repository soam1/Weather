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

    fun setUnit(value: String) {
        val editor = sharedPreferences.edit()
        editor.putString("unit", value)
        editor.apply()
    }

    fun getUnit(): String? {
        return sharedPreferences.getString("unit", null)
    }

    fun setUpdateTime(value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("update_time", value)
        editor.apply()
    }

    fun getUpdateTime(): Long {
        return sharedPreferences.getLong("update_time", 0)
    }


}