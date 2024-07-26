package com.example.weatherappkotlin.worker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherappkotlin.data.DailySummary
import com.example.weatherappkotlin.data.DailySummaryDao
import com.example.weatherappkotlin.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class DailySummaryWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val dailySummaryDao: DailySummaryDao,
    private val weatherRepository: WeatherRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val today = LocalDate.now()
            val weatherData = weatherRepository.getSummaryByDate(today)
            if (weatherData != null) {
                val dailySummary = calculateDailySummary(weatherData)
                dailySummaryDao.insert(dailySummary)
                sendBroadcast(dailySummary)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun calculateDailySummary(weatherData: DailySummary): DailySummary {
        val avgTemperature = weatherData.avgTemperature
        val maxTemperature = weatherData.maxTemperature
        val minTemperature = weatherData.minTemperature
        val dominantCondition = weatherData.dominantCondition

        return DailySummary(
            date = LocalDate.now(),
            avgTemperature = avgTemperature,
            maxTemperature = maxTemperature,
            minTemperature = minTemperature,
            dominantCondition = dominantCondition
        )
    }

    private fun sendBroadcast(dailySummary: DailySummary) {
        val intent = Intent("com.example.weatherappkotlin.DAILY_SUMMARY_UPDATE").apply {
            putExtra("date", dailySummary.date.toString())
            putExtra("avgTemperature", dailySummary.avgTemperature)
            putExtra("maxTemperature", dailySummary.maxTemperature)
            putExtra("minTemperature", dailySummary.minTemperature)
            putExtra("dominantCondition", dailySummary.dominantCondition)
        }
        applicationContext.sendBroadcast(intent)
        Log.d("DailySummaryWorker", "Broadcast sent with data: $dailySummary")

    }
}