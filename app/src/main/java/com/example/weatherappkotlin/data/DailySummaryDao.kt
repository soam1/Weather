package com.example.weatherappkotlin.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDate

@Dao
interface DailySummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summary: DailySummary)

    @Query("SELECT * FROM daily_summary WHERE date = :date")
    fun getSummaryByDate(date: LocalDate): DailySummary?

    @Query("SELECT * FROM daily_summary")
    fun getAllSummaries(): List<DailySummary>
}