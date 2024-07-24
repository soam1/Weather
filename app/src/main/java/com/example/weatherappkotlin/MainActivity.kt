package com.example.weatherappkotlin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherappkotlin.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class MainActivity : AppCompatActivity() {
    val API_KEY = "af122f1dc6328450b02797c8e139b1d0"
    val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var sharedPref: SharedPref

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var lastCity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        sharedPref = SharedPref(this)


        if (sharedPref.getLastCity() != null) {
            lastCity = sharedPref.getLastCity().toString()
            fetchWeatherData(lastCity)
        } else {
            lastCity = "Meerut"
            sharedPref.saveLastCity(lastCity)
            fetchWeatherData("Meerut")
        }
//        lastCity = "Meerut"
//        sharedPref.saveLastCity(lastCity)
//        fetchWeatherData("Meerut")
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        searchCity()
        //10 mins for now
        if (sharedPref.getUpdateTime() != 0L)
            scheduleWeatherDataFetch(sharedPref.getUpdateTime())
        else
            scheduleWeatherDataFetch(600000)
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    lastCity = query
                    sharedPref.saveLastCity(query)
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName: String) {
        Log.d("tag", "inside fetchWeatherData")
        val retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(cityName, API_KEY, "metric")
        response.enqueue(object : retrofit2.Callback<WeatherItem> {
            override fun onResponse(
                call: retrofit2.Call<WeatherItem>, response: retrofit2.Response<WeatherItem>
            ) {
                val weatherDataResponseBody = response.body()
                if (response.isSuccessful && weatherDataResponseBody != null) {
                    // Use the weatherData as needed
                    val temperature = weatherDataResponseBody.main.temp.toString()
                    val humidity = weatherDataResponseBody.main.humidity.toString()
                    val windSpeed = weatherDataResponseBody.wind.speed.toString()
                    val sunrise = weatherDataResponseBody.sys.sunrise.toLong()
                    val sunset = weatherDataResponseBody.sys.sunset.toLong()
                    val seaLevel = weatherDataResponseBody.main.pressure.toString()
                    val condition = weatherDataResponseBody.weather[0].main
                    val maxTemp = weatherDataResponseBody.main.temp_max.toString()
                    val minTemp = weatherDataResponseBody.main.temp_min.toString()
                    val feelsLike = weatherDataResponseBody.main.feels_like.toString()


//                    Log.d("tag", "Temperature: $temperature")
                    binding.temp.text = "$temperature °C"
                    binding.weather.text = condition
                    binding.max.text = "Max: $maxTemp °C"
                    binding.min.text = "Min: $minTemp °C"
                    binding.feelslike.text = "feels like: $feelsLike °C"
                    binding.humidity.text = "$humidity %"
                    binding.sea.text = "$seaLevel hPa"
                    binding.windspeed.text = "$windSpeed m/s"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text = "${time(sunset)}"
                    binding.condition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
//                    binding.date.text = getDate()
                    binding.date.text = date()
                    binding.cityName.text = weatherDataResponseBody.name
//                        binding.cityName.text = cityName
                    binding.updated.text = "Updated at: ${time(System.currentTimeMillis())}"
                    val lastUpdateTime = time(weatherDataResponseBody.dt.toLong())
                    binding.updated.text =
                        "Last updated at: ${lastUpdateTime}"

                    sharedPref.saveLastCity(weatherDataResponseBody.name)
                    changeImagesAccordingToWeatherCondition(condition)

                }
            }

            override fun onFailure(call: retrofit2.Call<WeatherItem>, t: Throwable) {
                Log.d("tag", "inside onFailure")
                Log.d("tag", "Error: ${t.message}")
//                Toast.makeText(this, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun changeImagesAccordingToWeatherCondition(condition: String) {
        when (condition) {


            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy", "Haze" -> {
                binding.root.setBackgroundResource(R.drawable.cloudy_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Light Rain", "Drizzle", "Freezing Rain", "Showers", "Moderate Rain", "Heavy Rain", "Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)

            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    fun dayName(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun getDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun date(): String {
        val sdf = java.text.SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun time(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(java.util.Date(timestamp * 1000))
    }


    private fun scheduleWeatherDataFetch(interval: Long) {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
//                if (::lastCity.isInitialized)
//                    fetchWeatherData(lastCity) // Replace "YourCityName" with the actual city name or a variable
//                else fetchWeatherData("Meerut")
//                lastCity = sharedPref.getLastCity().toString()
                fetchWeatherData(lastCity)
                handler.postDelayed(this, interval)
            }
        }
        handler.post(runnable)
    }

    private fun stopWeatherDataFetch() {
        handler.removeCallbacks(runnable)
    }
}