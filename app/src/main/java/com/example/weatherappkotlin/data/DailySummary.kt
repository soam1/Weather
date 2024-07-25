package com.example.weatherappkotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "daily_summary")
data class DailySummary(

    @PrimaryKey val date: LocalDate,
    val avgTemperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val dominantCondition: String
)