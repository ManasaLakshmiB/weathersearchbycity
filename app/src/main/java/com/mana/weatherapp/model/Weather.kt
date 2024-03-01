package com.mana.weatherapp.model

data class Weather(
    val base: String?=null,
    val clouds: Clouds?=null,
    val cod: Int?=null,
    val coord: Coord?=null,
    val dt: Int?=null,
    val id: Int?=null,
    val main: Main?=null,
    val name: String?=null,
    val sys: Sys?=null,
    val timezone: Int?=null,
    val visibility: Int?=null,
    val weather: List<WeatherItem>?=null,
    val wind: Wind?=null
)




