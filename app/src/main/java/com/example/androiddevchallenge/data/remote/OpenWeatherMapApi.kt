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
import com.example.androiddevchallenge.data.remote.model.Forecast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") appid: String = OpenWeatherMapApi.appid,
        @Query("units") units: String = "metric",
    ): Forecast

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        private var appid: String = ""

        private val client = OkHttpClient
            .Builder()
            .build()

        private val moshi = Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        fun create(appid: String): OpenWeatherMapApi {
            this.appid = appid
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(OpenWeatherMapApi::class.java)
        }

        fun createMock(context: Context) = MockWeatherApi(context, moshi)
    }
}
