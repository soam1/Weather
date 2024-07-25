package com.example.weatherappkotlin.repository

import com.example.weatherappkotlin.data.DailySummary
import com.example.weatherappkotlin.data.DailySummaryDao
import java.time.LocalDate

class WeatherRepository(private val dailySummaryDao: DailySummaryDao) {

    fun insertDailySummary(summary: DailySummary) {
        dailySummaryDao.insert(summary)
    }

    fun getSummaryByDate(date: LocalDate): DailySummary? {
        return dailySummaryDao.getSummaryByDate(date)
    }

    fun getAllSummaries(): List<DailySummary> {
        return dailySummaryDao.getAllSummaries()
    }
}
