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

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.data.local.model.LocationWeather
import com.example.androiddevchallenge.ui.theme.slide1
import com.example.androiddevchallenge.ui.theme.slide2
import com.example.androiddevchallenge.ui.theme.slide3
import com.example.androiddevchallenge.ui.theme.slide4
import com.google.accompanist.coil.CoilImage
import kotlin.math.roundToInt

@Composable
fun TopInfo(weather: LocationWeather) {
    Row(Modifier.fillMaxWidth()) {
        Column(Modifier.weight(3f)) {

            Row {
                WeatherCloudy(
                    modifier = Modifier
                        .padding(8.dp, 4.dp)
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary),
                    size = 100
                ) {
                    WeatherSunny(size = 80)
                    WeatherCloud(size = 100)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = weather.forecast.list[0].dtTxt,
                        modifier = Modifier.padding(8.dp, 4.dp),
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = weather.forecast.list[0].weather[0].main,
                        modifier = Modifier.padding(8.dp, 4.dp),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }

        Text(
            modifier = Modifier
                .weight(2f)
                .padding(16.dp, 8.dp),
            text = "${weather.forecast.list[0].main.temp.roundToInt()} \u2103",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun MainPicture(imageUrl: String, city: String) {
    CoilImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
            .clip(RoundedCornerShape(12.dp)),
        data = imageUrl,
        contentScale = ContentScale.Fit,
        contentDescription = "$city image",
    )
}

@Composable
fun MainCards(state: State) {

    var weights by remember {
        mutableStateOf(
            listOf(
                BoxState.Expanded,
                BoxState.Collapsed,
                BoxState.Collapsed,
                BoxState.Collapsed
            )
        )
    }

    val clicked = { number: Int ->
        weights = weights.mapIndexed { index, fl ->
            if (index == number) {
                BoxState.Expanded
            } else {
                BoxState.Collapsed
            }
        }
    }

    val weightTransition = updateTransition(weights)

    val weightVals = weights.map { state ->
        val weight by weightTransition.animateFloat {
            when (state) {
                BoxState.Collapsed -> 0f
                BoxState.Expanded -> 1f
            }
        }
        weight
    }

    fun Float.slowDown() = if (this < 0.5f) 0f else (this - 0.5f).times(2f)

    val alphaVals = weightVals.map { it.slowDown() }

    BoxWithConstraints {

        val height = with(LocalDensity.current) { 250.dp.toPx() }

        val expandedWidth = this.maxWidth.div(2)
        val collapsedWidth = this.maxWidth.div(6)

        val expandedWidthPx = with(LocalDensity.current) { expandedWidth.toPx() }
        val collapsedWidthPx = with(LocalDensity.current) { collapsedWidth.toPx() }

        Row(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        ) {

            CollapsingLayout(
                expandedWidthPx,
                collapsedWidthPx,
                height,
                weightVals[0],
                modifier = Modifier
                    .background(slide1)
                    .clickable { clicked(0) }
            ) {
                val modifier = Modifier.alpha(alphaVals[0])

                Text(text = "-1 ℃")
                WeatherSunny(modifier)
                Text(text = "Morning", modifier = modifier)
                Text(text = "Sunny", modifier = modifier)
            }

            CollapsingLayout(
                expandedWidthPx,
                collapsedWidthPx,
                height,
                weightVals[1],
                modifier = Modifier
                    .background(slide2)
                    .clickable { clicked(1) }
            ) {
                val modifier = Modifier.alpha(alphaVals[1])

                Text(text = "2 ℃")
                WeatherCloudy(modifier)
                Text(text = "Day", modifier = modifier)
                Text(text = "Cloudy", modifier = modifier)
            }

            CollapsingLayout(
                expandedWidthPx,
                collapsedWidthPx,
                height,
                weightVals[2],
                modifier = Modifier
                    .background(slide3)
                    .clickable { clicked(2) }
            ) {
                val modifier = Modifier.alpha(alphaVals[2])

                Text(text = "1 ℃")
                WeatherRaining(modifier)
                Text(text = "Evening", modifier = modifier)
                Text(text = "Raining", modifier = modifier)
            }

            CollapsingLayout(
                expandedWidthPx,
                collapsedWidthPx,
                height,
                weightVals[3],
                modifier = Modifier
                    .background(slide4)
                    .clickable { clicked(3) }
            ) {
                val modifier = Modifier.alpha(alphaVals[3])

                Text(text = "-4 ℃")
                WeatherSnowingNight(modifier)
                Text(text = "Night", modifier = modifier)
                Text(text = "Snowing", modifier = modifier)
            }
        }
    }
}

@Composable
private fun CollapsingLayout(
    expandedSize: Float,
    collapsedSize: Float,
    height: Float,
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size >= 2)

        val widthDiff = expandedSize - collapsedSize
        val widthOffset = widthDiff * collapseFraction

        val tempText = measurables[0].measure(constraints)
        val tempPadding = (collapsedSize - tempText.width).div(2)
        val tempStart = (widthOffset * 0.5f).roundToInt() + tempPadding.roundToInt()

        val image = measurables[1].measure(constraints)
        val imageStart = (widthOffset + collapsedSize - image.width).div(2)

        val restOfPlaceables = measurables
            .subList(2, measurables.size)
            .map { it.measure(constraints) }

        val collapsedStartHeight = height.roundToInt().div(2)
        val expandedStartHeight = height.roundToInt().div(4)

        val tempHeight = (expandedStartHeight * (2 + 0.5 * collapseFraction)).roundToInt()

        var yPosition = tempHeight

        layout(
            width = (collapsedSize + widthOffset).roundToInt(),
            height = height.roundToInt()
        ) {
            image.place(imageStart.roundToInt(), 0)
            tempText.place(
                tempStart,
                yPosition - tempText.height.div(2)
            )

            yPosition += tempText.height.div(2)

            restOfPlaceables
                .forEach { placeable ->
                    // Position item on the screen
                    placeable.place(
                        x = tempStart,
                        y = yPosition
                    )

                    // Record the y co-ord placed up to
                    yPosition += placeable.height
                }
        }
    }
}

private enum class BoxState {
    Collapsed,
    Expanded,
}
