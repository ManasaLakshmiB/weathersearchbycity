package com.mana.weatherapp.repository

import android.util.Log
import com.mana.weatherapp.data.DataOrException
import com.mana.weatherapp.model.Weather
import com.mana.weatherapp.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {

    suspend fun getWeather(cityQuery: String)
            : DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeather(query = cityQuery)

        } catch (e: Exception) {
            Log.d("REX", "getWeather: $e")
            return DataOrException(e = e)
        }
        Log.d("INSIDE", "getWeather: $response")
        return DataOrException(data = response)

    }


    suspend fun getWeatherLatLong(lat:Double,long:Double)
            : DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeatherLatLong(lat = lat, long = long)
        } catch (e: Exception) {
            Log.d("REX", "getWeather: $e")
            return DataOrException(e = e)
        }
        Log.d("INSIDE", "getWeather: $response")
        return DataOrException(data = response)

    }

}