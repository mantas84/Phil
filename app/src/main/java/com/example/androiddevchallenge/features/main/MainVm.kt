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
package com.example.androiddevchallenge.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.Repository
import com.example.androiddevchallenge.data.local.model.LocationWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVm @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _state: MutableStateFlow<State> =
        MutableStateFlow(State(false, emptyList(), null, null))

    val state: StateFlow<State>
        get() = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(State(true, emptyList(), null, null))
            val weather = repository.getWeather()
            _state.emit(
                State(
                    false, weather, 0, 0, mainImage = map[weather[0].city]
                )
            )
        }
    }

    fun selected(selectedId: Int) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    selectedId = selectedId,
                    mainImage = map[state.value.weatherList[selectedId].city]
                )
            )
        }
    }

    private val map = mapOf(
        "Vilnius" to "https://images.unsplash.com/photo-1444487250222-33a851d3e461?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1267&q=80",
        "London" to "https://images.unsplash.com/photo-1505761671935-60b3a7427bad?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        "New York" to "https://images.unsplash.com/photo-1499092346589-b9b6be3e94b2?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1351&q=80",
        "Paris" to "https://images.unsplash.com/photo-1431274172761-fca41d930114?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        "Rome" to "https://images.unsplash.com/photo-1552832230-c0197dd311b5?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1286&q=80",
    )
}

data class State(
    val isLoading: Boolean,
    val weatherList: List<LocationWeather>,
    val selectedId: Int? = null,
    val selectedTime: Int? = null,
    val mainImage: String? = null
)
