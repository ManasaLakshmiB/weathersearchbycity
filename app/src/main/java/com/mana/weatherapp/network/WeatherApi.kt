package com.mana.weatherapp.network

import com.mana.weatherapp.model.Weather
import com.mana.weatherapp.utils.Constants.API_KEY
import dagger.Binds
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherApi {
    @GET(value = "data/2.5/weather?")
    suspend fun getWeather(
        @Query("q") query: String,
        @Query("appid") appid: String = API_KEY // your api key
    ): Weather


    @GET(value = "data/2.5/weather?")
    suspend fun getWeatherLatLong(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appid: String = API_KEY
    ): Weather
}