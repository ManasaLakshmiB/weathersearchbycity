package com.mana.weatherapp

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mana.weatherapp.data.WeatherDao
import com.mana.weatherapp.data.WeatherDatabase
import com.mana.weatherapp.model.Favorite
import com.mana.weatherapp.repository.WeatherDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch


class WeatherDatabaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var db: WeatherDatabase
    lateinit var weatherDao: WeatherDao
    lateinit var repository: WeatherDbRepository


    @Before
    fun setUp() {
    db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        WeatherDatabase::class.java
    ).allowMainThreadQueries().build()
        weatherDao = db.weatherDao()
        repository = WeatherDbRepository(weatherDao)
    }


    @Test
    fun insertCity() = runTest {
        val data = Favorite("ABC","India")
        weatherDao.insertFavorite(favorite = data)
        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            weatherDao.getFavorites().collect {
                assertEquals(1,it.size)
                latch.countDown()

            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @After
    fun tearDown(){
        db.close()
    }
}