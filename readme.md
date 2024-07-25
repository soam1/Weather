# Weather Application

## Overview

This is a comprehensive weather application built for Android using Kotlin. It allows users to view
daily weather summaries, including average temperature, maximum and minimum temperatures, and
dominant weather conditions. The app also provides visualizations of weather data using charts to
offer a better understanding of the weather trends. Additionally, the app allows user-configurable
update times (e.g., every 5 minutes or every 30 minutes) and offers vibration alerts (
user-configurable for max and min temperature) for temperature thresholds.

## Features

- **Current Weather**: View the current weather conditions, including temperature, humidity, and
  wind speed.
- **7-Day Forecast**: Get a forecast for the next seven days with detailed daily summaries.
- **Weather Visualizations**: Display weather data using line charts for temperatures and pie charts
  for weather conditions.
- **Persistent Storage**: Store daily weather summaries in a local Room database for offline access
  and future analysis.
- **User-Configurable Update Time**: Set the weather data update interval (e.g., every 5/10/30/60
  minutes).
- **Vibration Alerts**: Receive vibration alerts for temperature thresholds (configurable for max
  and min temperature).

## Architecture

This application follows the MVVM (Model-View-ViewModel) architectural pattern to separate the UI
components from the business logic and data access. Here's a brief overview of the architecture:

- **Model**: Represents the data layer, including the Room database and Retrofit network API.
- **View**: Consists of UI components and layouts.
- **ViewModel**: Handles UI-related data and provides data to the UI components.
- **Repository**: Acts as a single source of truth, mediating between the data sources (database,
  network) and the ViewModel.

## Project Structure

```
/src
  /main
    /java
      /com
        /example
          /weatherapp
            /data
              - WeatherDatabase.kt
              - DailySummary.kt
              - DailySummaryDao.kt
              - Converters.kt
            /repository
              - WeatherRepository.kt
            /network
              - WeatherApiService.kt
              - WeatherApiClient.kt
            /ui
              /summary
                - WeatherSummaryActivity.kt
            /viewmodel
              - WeatherSummaryViewModel.kt
    /res
      /layout
        - activity_weather_summary.xml
      /drawable
        - weather_icon.png
```

## Getting Started

### Prerequisites

Before you begin, ensure you have met the following requirements:

- Android Studio Arctic Fox (or newer)
- Android SDK 21+
- An internet connection for fetching weather data

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/soam1/Weather.git
   cd Weather
   ```

2. **Open in Android Studio**:

    - Launch Android Studio.
    - Select "Open an existing Android Studio project."
    - Navigate to the cloned directory and select it.

3. **Build the Project**:

    - Click on "Build" > "Make Project" to build the application.

4. **Run the Application**:

    - Connect an Android device or start an emulator.
    - Click the "Run" button to deploy the app.

### Configuration

#### API Key

To fetch weather data, you need to obtain an API key from a weather service provider
like [OpenWeatherMap](https://openweathermap.org/). Add the API key to your project:

1. Open the `WeatherApiClient.kt` file located in the `/network` package.
2. Locate the `API_KEY` constant and replace the placeholder with your actual API key.

   ```kotlin
   const val API_KEY = "YOUR_API_KEY"
   ```

#### Permissions

Ensure the following permissions are added in the `AndroidManifest.xml` file to access the internet:

```xml

<uses-permission android:name="android.permission.INTERNET" /><uses-permission
android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Detailed Component Breakdown

### Data Layer

1. **Room Database**:
    - **`DailySummary.kt`**: Entity class representing a daily weather summary.
    - **`DailySummaryDao.kt`**: Data Access Object for CRUD operations.
    - **`WeatherDatabase.kt`**: Abstract class extending `RoomDatabase`.

2. **Network**:
    - **`WeatherApiService.kt`**: Defines the endpoints for fetching weather data using Retrofit.
    - **`WeatherApiClient.kt`**: Provides an instance of `WeatherApiService`.

### Repository

- **`WeatherRepository.kt`**: Handles data operations and abstracts the data sources from the rest
  of the app.

### ViewModel

- **`WeatherSummaryViewModel.kt`**: Manages UI-related data and acts as a bridge between the
  repository and UI.

### UI Layer

1. **Activity**:
    - **`WeatherSummaryActivity.kt`**: Displays weather data and handles user interactions.

2. **Layouts**:
    - **`activity_weather_summary.xml`**: Layout file for the `WeatherSummaryActivity`.

### Visualization

The app uses [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) library for visualizing
weather data:

- **Line Chart**: Displays average temperature trends over days.
- **Pie Chart**: Shows the distribution of weather conditions (e.g., sunny, rainy, cloudy).

## Usage

1. **Viewing Current Weather**: Launch the app to view the current weather conditions.
2. **Exploring Forecast**: Navigate to the forecast section to see upcoming weather trends.
3. **Analyzing Charts**: Use the temperature and condition charts to analyze historical weather
   data.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more
information.

## Contributing

Contributions are welcome! Follow these steps to contribute:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/YourFeature`.
3. Commit your changes: `git commit -m 'Add YourFeature'`.
4. Push to the branch: `git push origin feature/YourFeature`.
5. Open a pull request.

## Acknowledgments

- **OpenWeatherAPI**: For providing weather data.
- **MPAndroidChart**: For charting solutions.
- **Android Developers**: For documentation and resources.

## Screenshots

![Screenshot 1](/screenshots/ss1.jpg)
![Screenshot 2](/screenshots/ss2.jpg)
![Screenshot 3](/screenshots/ss3.jpg)
![Screenshot 4](/screenshots/ss4.jpg)
![Screenshot 5](/screenshots/ss5.jpg)
![Screenshot 6](/screenshots/ss6.jpg)
![Screenshot 7](/screenshots/ss7.jpg)
![Screenshot 8](/screenshots/ss8.jpg)
![Screenshot 9](/screenshots/ss9.jpg)
![Screenshot 10](/screenshots/ss10.jpg)
![Screenshot 11](/screenshots/ss11.jpg)
![Screenshot 12](/screenshots/ss12.jpg)
![Screenshot 13](/screenshots/ss13.jpg)
![Screenshot 14](/screenshots/ss14.jpg)
![Screenshot 15](/screenshots/ss15.png)


