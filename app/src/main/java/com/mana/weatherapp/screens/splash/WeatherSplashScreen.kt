package com.mana.weatherapp.screens.splash

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mana.weatherapp.R
import com.mana.weatherapp.navigation.WeatherScreens
import com.mana.weatherapp.utils.PreferencesManager
import kotlinx.coroutines.delay

@Composable
fun WeatherSplashScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val data = remember { mutableStateOf(preferencesManager.getData("myKey", "")) }

    var  defaultCity:String
    if (data.value.isNullOrBlank()){
        defaultCity = "hello"
    }else{
        defaultCity = data.value
    }

    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }
            )
        )
        delay(2000L)
        navController.navigate(route = WeatherScreens.MainScreen.name+"/$defaultCity")
    })

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale = scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(
            width = 2.dp,
            color = Color.LightGray
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.partly_cloudy_day),
                contentScale = ContentScale.Fit, contentDescription = "image"
            )
            Text(text = "Weather App", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black))


        }

    }
}