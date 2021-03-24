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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
@Preview
fun MainScreen(mainVm: MainVm = viewModel()) {

    val state = mainVm.state.collectAsState().value

    LazyColumn(Modifier.fillMaxWidth()) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                state.weatherList.forEachIndexed { index, locationWeather ->
                    CityItem(
                        selected = index == state.selectedId,
                        cityName = locationWeather.city
                    ) { mainVm.selected(index) }
                }
            }
        }

        if (state.selectedId != null) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                TopInfo(state.weatherList[state.selectedId])
            }

            item {
                MainPicture(state.mainImage!!, state.weatherList[state.selectedId].city)
            }

            item {
                MainCards(state)
            }
        }
    }
}

@Composable
@Preview
fun CityItem(selected: Boolean = true, cityName: String = "Vilnius", onClick: () -> Unit = {}) {

    var isSelected by remember { mutableStateOf(false) }

    isSelected = selected

    val backgroundColor by animateColorAsState(
        if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(12.dp, 8.dp)
            .clickable {
                isSelected = true
                onClick()
            }
    ) {
        Text(text = cityName)
    }
}
