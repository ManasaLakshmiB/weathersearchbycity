package com.mana.weatherapp.widgets

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.mana.weatherapp.data.DataOrException
import com.mana.weatherapp.model.Weather
import com.mana.weatherapp.screens.main.MainScaffold
import com.mana.weatherapp.screens.main.MainViewModel

@Composable
fun LocationScreen(onLatlong: (String)-> Unit,locationClick: @Composable () -> Unit) {
    val context = LocalContext.current
    var location = remember { mutableStateOf("Your location") }

    // Create a permission launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted, update the location
                    getCurrentLocation(context) { lat, long ->
                        location.value = "$lat,$long"
                        onLatlong.invoke(location.value)
                        locationClick
                        Log.d("TAG", "LocationScreen: Latitude: $lat, Longitude: $long")

                    }
                }
            })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Button(
            onClick = {
                if (hasLocationPermission(context)) {
                    // Permission already granted, update the location
                    getCurrentLocation(context) { lat, long ->
                        location.value = "$lat,$long"
                        onLatlong.invoke(location.value)

                    }
                } else {
                    // Request location permission
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        ) {
            Text(text = "Allow Permission")

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Location Data on Lat & Long", style = MaterialTheme.typography.titleLarge)
        if (location.value.isNotEmpty()){
            locationClick()
        }
      //  Text(text = location.value)
    }
}

private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long)
            }
        }
        .addOnFailureListener { exception ->
            // Handle location retrieval failure
            exception.printStackTrace()
        }
}