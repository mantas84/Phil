/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.data.remote

import android.content.Context
import android.content.res.AssetManager
import com.example.androiddevchallenge.data.remote.model.Forecast
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay

class MockWeatherApi(context: Context, moshi: Moshi) : OpenWeatherMapApi {

    private val assetManager = context.assets
    private val jsonAdapter: JsonAdapter<Forecast> = moshi.adapter(Forecast::class.java)

    private val vilnius: Forecast = jsonAdapter.forecastFromAsset(fileName = "vilnius.json")
    private val london: Forecast = jsonAdapter.forecastFromAsset(fileName = "london.json")
    private val newYork: Forecast = jsonAdapter.forecastFromAsset(fileName = "new_york.json")
    private val paris: Forecast = jsonAdapter.forecastFromAsset(fileName = "paris.json")
    private val rome: Forecast = jsonAdapter.forecastFromAsset(fileName = "rome.json")

    private val map = mapOf(
        "vilnius" to vilnius,
        "london" to london,
        "new york" to newYork,
        "paris" to paris,
        "rome" to rome,
    )

    private suspend fun getForecast(cityName: String, delayTime: Long = 0L): Forecast {
        delay(delayTime)
        return map.filterKeys { it.contains(cityName, true) }.map { it.value }.first()
    }

    private fun JsonAdapter<Forecast>.forecastFromAsset(fileName: String): Forecast {
        return this.fromJson(assetManager.readAssetsFile(fileName = fileName))!!
    }

    private fun AssetManager.readAssetsFile(fileName: String): String {
        return open(fileName).bufferedReader().use { it.readText() }
    }

    override suspend fun getForecast(cityName: String, appid: String, units: String): Forecast {
        return getForecast(cityName)
    }
}
