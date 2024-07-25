package com.example.weatherappkotlin.ui.summary


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappkotlin.data.DailySummary
import com.example.weatherappkotlin.repository.WeatherRepository
import java.time.LocalDate

class WeatherSummaryViewModel(private val repository: WeatherRepository) : ViewModel() {

    // Insert a daily summary directly in the foreground
    fun insertDailySummary(summary: DailySummary) {
        repository.insertDailySummary(summary)
    }

    // Retrieve all summaries directly in the foreground
    fun getAllSummaries(): List<DailySummary> {
        return repository.getAllSummaries()
    }

    // return today's summary
    // Return today's summary
    @RequiresApi(Build.VERSION_CODES.O)
    fun getSummaryByDate(date: LocalDate): DailySummary? {
//        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//        val dateString = date.format(formatter)
        return repository.getSummaryByDate(date)
    }

    // Factory for creating the ViewModel with a repository parameter
    class Factory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherSummaryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatherSummaryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
