package com.mana.weatherapp.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mana.weatherapp.data.DataOrException
import com.mana.weatherapp.model.Weather
import com.mana.weatherapp.navigation.WeatherScreens
import com.mana.weatherapp.utils.PreferencesManager
import com.mana.weatherapp.utils.formatDate
import com.mana.weatherapp.widgets.LocationScreen
import com.mana.weatherapp.widgets.WeatherAppBar

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    city: String?
) {
    val context = LocalContext.current
    var lat = 0.0
    var long = 0.0
    val preferencesManager = remember { PreferencesManager(context) }
    var   weatherData = remember { DataOrException<Weather, Boolean, Exception>() }


    weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = mainViewModel.getWeatherData(city.toString())
    }.value

    if (weatherData.loading == true) {
        CircularProgressIndicator()
    } else if (weatherData.data != null) {
        MainScaffold(weather = weatherData.data!!, navController = navController)
        preferencesManager.saveData("myKey", city.toString())
    } else {
        MainScaffold(weather = Weather(), navController = navController)
    }


    LocationScreen(onLatlong = {
        val latlong = it.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        lat = latlong[0].toDouble()
        long = latlong[1].toDouble()

    }, locationClick = {

        weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = mainViewModel.getWeatherLatLongData(lat, long)
        }.value

        if (weatherData.data != null) {

            Column(modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start) {

            }
            Text(text = "City Name : "+weatherData.data?.name.toString())
            Text(text = "Country Name : "+weatherData.data?.sys?.country.toString())
            Text(text = "Temp : "+weatherData.data?.main?.temp.toString())
            Text(text = "Humidty : "+weatherData.data?.main?.humidity.toString())
            Text(text = "Weather : "+weatherData.data?.weather?.get(0)?.main.toString())
         }
    })

}

@Composable
fun MainScaffold(weather: Weather, navController: NavController) {
    Scaffold(topBar = {
        Surface(shadowElevation = 10.dp, modifier = Modifier.padding(8.dp)) {
            if (weather.name.isNullOrBlank()) {
                WeatherAppBar(
                    title = "",
                    navController = navController,
                    onAddActionClicked = {
                        navController.navigate(WeatherScreens.SearchScreen.name)
                    },
                )
            } else {
                WeatherAppBar(
                    title = weather.name + ",${weather.sys?.country}",
                    navController = navController,
                    onAddActionClicked = {
                        navController.navigate(WeatherScreens.SearchScreen.name)
                    },
                )
            }

        }
    }) {
        it
        if (weather.dt == null) {
            Box {}
        } else {
            MainContent(data = weather, it)

        }
    }

}

@Composable
fun MainContent(data: Weather, paddingValues: PaddingValues) {

    val imageUrl = "https://openweathermap.org/img/wn/${data.weather?.get(0)?.icon}.png"
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = formatDate(data.dt!!),
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(6.dp)
        )

        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(200.dp),
            shape = CircleShape, shadowElevation = 5.dp,
            color = Color(0XFFFFC400)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(
                    text = data.main?.temp.toString() + "°",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                )
                Text(
                    text = data.weather!![0]?.main.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,

                    )


            }

        }

        HumidityWindPressureRow(weather = data)

        Divider(Modifier.height(2.dp))
    }

}

@Composable
fun HumidityWindPressureRow(weather: Weather) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Humidity : ${weather.main?.humidity}",
                style = MaterialTheme.typography.titleSmall
            )

        }
        Row(modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Pressure : ${weather.main?.pressure}",
                style = MaterialTheme.typography.titleSmall
            )

        }

        Row(modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Wind Speed : ${weather.wind?.speed}",
                style = MaterialTheme.typography.titleSmall
            )

        }
    }
}

@Composable
fun WeatherStateImage(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl), contentDescription = "icon image",
        modifier = Modifier.size(80.dp)
    )
}
