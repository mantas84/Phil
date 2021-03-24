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
package com.example.androiddevchallenge.data.local

import androidx.room.TypeConverter
import com.example.androiddevchallenge.data.remote.model.Forecast
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {

    private val moshi = Moshi
        .Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val jsonAdapter: JsonAdapter<Forecast> = moshi.adapter(Forecast::class.java)

    @TypeConverter
    fun forecastToString(forecast: Forecast): String = jsonAdapter.toJson(forecast)

    @TypeConverter
    fun stringToForecast(value: String): Forecast = jsonAdapter.fromJson(value)!!
}
