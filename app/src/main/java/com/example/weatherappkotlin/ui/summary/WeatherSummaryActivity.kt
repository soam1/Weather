package com.example.weatherappkotlin.ui.summary

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.weatherappkotlin.data.DailySummary
import com.example.weatherappkotlin.database.WeatherDatabase
import com.example.weatherappkotlin.databinding.ActivityWeatherSummaryBinding
import com.example.weatherappkotlin.repository.WeatherRepository
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import java.time.LocalDate

class WeatherSummaryActivity : AppCompatActivity() {

    private lateinit var weatherDatabase: WeatherDatabase
    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherSummaryViewModel
    private val binding: ActivityWeatherSummaryBinding by lazy {
        ActivityWeatherSummaryBinding.inflate(layoutInflater)
    }

    private val dailySummaryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val date = LocalDate.parse(it.getStringExtra("date"))
                val avgTemperature = it.getDoubleExtra("avgTemperature", 0.0)
                val maxTemperature = it.getDoubleExtra("maxTemperature", 0.0)
                val minTemperature = it.getDoubleExtra("minTemperature", 0.0)
                val dominantCondition = it.getStringExtra("dominantCondition") ?: "Unknown"

                val dailySummary = DailySummary(
                    date = date,
                    avgTemperature = avgTemperature,
                    maxTemperature = maxTemperature,
                    minTemperature = minTemperature,
                    dominantCondition = dominantCondition
                )
                bindTodaySummary(dailySummary)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Register the broadcast receiver
        registerReceiver(
            dailySummaryReceiver,
            IntentFilter("com.example.weatherappkotlin.DAILY_SUMMARY_UPDATE"), RECEIVER_NOT_EXPORTED
        )

        // Initialize charts
        val temperatureChart: LineChart = binding.temperatureChart
        val conditionChart: PieChart = binding.conditionChart

        // Initialize database and repository
        weatherDatabase = Room.databaseBuilder(
            applicationContext,
            WeatherDatabase::class.java,
            "weather_db"
        ).allowMainThreadQueries().build()

        repository = WeatherRepository(weatherDatabase.dailySummaryDao())

        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this,
            WeatherSummaryViewModel.Factory(repository)
        )[WeatherSummaryViewModel::class.java]

        // Fetch today's summary and bind to CardView
        val todaySummary = viewModel.getSummaryByDate(LocalDate.now())
        todaySummary?.let { bindTodaySummary(it) }

        // Fetch and display all summaries
        val summaries = viewModel.getAllSummaries()
        setupTemperatureChart(temperatureChart, summaries)
        setupConditionChart(conditionChart, summaries)
    }

    private fun bindTodaySummary(summary: DailySummary) {
        binding.todayDate.text = summary.date.toString()
        binding.todayAvgTemperature.text = "Average Temperature: ${summary.avgTemperature}"
        binding.todayMaxTemperature.text = "Maximum Temperature: ${summary.maxTemperature}"
        binding.todayMinTemperature.text = "Minimum Temperature: ${summary.minTemperature}"
        binding.todayDominantCondition.text = "Dominant Condition: ${summary.dominantCondition}"
    }

    private fun setupTemperatureChart(chart: LineChart, summaries: List<DailySummary>) {
        val entries = summaries.map {
            com.github.mikephil.charting.data.Entry(
                it.date.toEpochDay().toFloat(),
                it.avgTemperature.toFloat()
            )
        }

        val dataSet =
            com.github.mikephil.charting.data.LineDataSet(entries, "Average Temperature").apply {
                color = android.graphics.Color.BLUE
                valueTextColor = android.graphics.Color.BLACK
            }

        chart.data = com.github.mikephil.charting.data.LineData(dataSet)
        chart.invalidate() // Refresh the chart
    }

    private fun setupConditionChart(chart: PieChart, summaries: List<DailySummary>) {
        val conditionCount = summaries.groupingBy { it.dominantCondition }.eachCount()

        val entries = conditionCount.map { (condition, count) ->
            com.github.mikephil.charting.data.PieEntry(count.toFloat(), condition)
        }

        val dataSet =
            com.github.mikephil.charting.data.PieDataSet(entries, "Weather Conditions").apply {
                colors = com.github.mikephil.charting.utils.ColorTemplate.MATERIAL_COLORS.toList()
            }

        chart.data = com.github.mikephil.charting.data.PieData(dataSet)
        chart.invalidate() // Refresh the chart
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(dailySummaryReceiver)
    }
}