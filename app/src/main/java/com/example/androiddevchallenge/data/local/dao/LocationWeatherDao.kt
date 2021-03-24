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
package com.example.androiddevchallenge.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.androiddevchallenge.data.local.model.LocationWeather

@Dao
interface LocationWeatherDao {

    @Insert
    suspend fun insertAll(weather: List<LocationWeather>)

    @Insert
    suspend fun insert(weather: LocationWeather)

    @Delete
    suspend fun delete(weather: LocationWeather)

    @Delete
    suspend fun delete(weather: List<LocationWeather>)

    @Query("SELECT * FROM location_weather")
    suspend fun getAll(): List<LocationWeather>

    @Query("SELECT * FROM location_weather where city like :cityName")
    suspend fun get(cityName: String): List<LocationWeather>

    @Query("SELECT * FROM location_weather where id = :id")
    suspend fun get(id: Int): List<LocationWeather>
}
