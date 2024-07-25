package com.example.weatherappkotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherappkotlin.data.DailySummary
import com.example.weatherappkotlin.data.DailySummaryDao
import com.example.weatherappkotlin.data.LocalDateConverter

@Database(entities = [DailySummary::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun dailySummaryDao(): DailySummaryDao
}