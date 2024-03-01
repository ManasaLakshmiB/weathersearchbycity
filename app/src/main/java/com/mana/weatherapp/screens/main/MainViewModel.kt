package com.mana.weatherapp.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mana.weatherapp.data.DataOrException
import com.mana.weatherapp.model.Weather
import com.mana.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    suspend fun getWeatherData(city: String)
            : DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather(cityQuery = city)

    }

    suspend fun getWeatherLatLongData(lat:Double,long:Double)
            : DataOrException<Weather, Boolean, Exception> {

        return repository.getWeatherLatLong(lat = lat, long = long)

    }
}