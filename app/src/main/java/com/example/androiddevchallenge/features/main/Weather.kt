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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun WeatherImage(modifier: Modifier) {
    Box(modifier = modifier) {
    }
}

@Composable
@Preview
fun WeatherSunny(modifier: Modifier = Modifier, size: Int = 100) {
    Box(modifier = modifier.size(size.dp)) {

        val infiniteTransition = rememberInfiniteTransition()
        val rotateDegree by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        Canvas(modifier = Modifier.size(size.dp)) {

            val radius = size.div(6)

            drawCircle(
                Color.Yellow,
                radius = radius.dp.toPx(),
                style = Stroke(width = size.div(15).dp.toPx())
            )

            fun drawSunLine() {
                drawLine(
                    Color.Yellow,
                    Offset(
                        (size.div(2) + 1.75f * radius).dp.toPx(),
                        size.div(2).dp.toPx()
                    ),
                    Offset(
                        (size.div(2) + 2.5f * radius).dp.toPx(),
                        size.div(2).dp.toPx()
                    ),
                    strokeWidth = size.div(15).dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            for (i in 0..7) {
                rotate(degrees = rotateDegree + 45f * i) {
                    drawSunLine()
                }
            }
        }
    }
}

@Composable
@Preview
fun WeatherCloud(modifier: Modifier = Modifier, size: Int = 100) {
    Box(modifier = modifier.size(size.dp)) {

        val half = with(LocalDensity.current) { size.div(2).dp.toPx() }
        val sixtyPercent = with(LocalDensity.current) { size.div(5).times(3).dp.toPx() }
        val full = with(LocalDensity.current) { size.dp.toPx() }

        val path = Path()
        path.addRoundRect(RoundRect(Rect(0f, half, full, full), CornerRadius(full)))
        path.addOval(Rect(Offset(sixtyPercent, half), full.div(4)))

        Canvas(modifier = Modifier.size(size.dp)) {
            this.drawPath(path, Color.Gray)
        }
    }
}

@Composable
@Preview
fun WeatherCloudy(
    modifier: Modifier = Modifier,
    size: Int = 100,
    content: @Composable () -> Unit = {
        WeatherSunny(size = 80)
        WeatherCloud(size = 100)
    }
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { it.measure(constraints) }

        layout(
            width = size.dp.toPx().roundToInt(),
            height = size.dp.toPx().roundToInt()
        ) {
            placeables.forEach { it.place(0, 0) }
        }
    }
}

@Composable
@Preview
fun WeatherRain(modifier: Modifier = Modifier, size: Int = 100) {
    Box(modifier = modifier.size(size.dp)) {

        val tenth = with(LocalDensity.current) { size.div(10).dp.toPx() }
        val movingXLenght = with(LocalDensity.current) { size.div(10).times(4).dp.toPx() }

        fun Float.norm(size: Float) = this.div(size)
        fun Float.alpha(size: Float): Float {
            val norm = this.norm(size)
            return if (norm < 0.5) 1f else 2 * (1f - norm)
        }

        val infiniteTransition = rememberInfiniteTransition()
        val movingOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = movingXLenght,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        fun Path.waterDrop(startX: Float, startY: Float) {
            val path = this
            path.moveTo(startX, startY)
            path.quadraticBezierTo(
                startX + 0.5f * tenth,
                startY + 1.25f * tenth,
                startX - 0.5f * tenth,
                startY + 1 * tenth
            )
            path.quadraticBezierTo(
                startX - tenth,
                startY + 0.5f * tenth,
                startX,
                startY
            )
        }

        val halfOffset = movingOffset.div(2)

        fun createLine(): Path {
            return Path().apply {
                waterDrop(3f * tenth - halfOffset, tenth + movingOffset)
                waterDrop(4.5f * tenth - halfOffset, 1.5f * tenth + movingOffset)
                waterDrop(6f * tenth - halfOffset, tenth + movingOffset)
                waterDrop(7.5f * tenth - halfOffset, 1.5f * tenth + movingOffset)
            }
        }

        Canvas(modifier = Modifier.size(size.dp)) {
            this.drawPath(createLine(), Color.Gray, alpha = movingOffset.alpha(movingXLenght))
        }
    }
}

@Composable
@Preview
fun WeatherSnow(modifier: Modifier = Modifier, size: Int = 100) {
    Box(modifier = modifier.size(size.dp)) {

        val fivePercent = with(LocalDensity.current) { size.div(20).dp.toPx() }
        val strokeWidth = with(LocalDensity.current) { size.div(50).dp.toPx() }
        val movingXLength = with(LocalDensity.current) { size.div(10).times(4).dp.toPx() }

        fun Float.norm(size: Float) = this.div(size)
        fun Float.alpha(size: Float): Float {
            val norm = this.norm(size)
            return if (norm < 0.5) 1f else 2 * (1f - norm)
        }

        val infiniteTransition = rememberInfiniteTransition()
        val movingOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = movingXLength,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val alpha = movingOffset.alpha(movingXLength)

        Canvas(modifier = Modifier.size(size.dp)) {

            fun drawSnowFlake(centerX: Float, centerY: Float, radius: Float) {
                val halfRadius = radius.div(1.5f)
                val reducedX = movingOffset.div(5)
                val sin = Math.cos(Math.toRadians(360.0 * movingOffset.norm(movingXLength)))
                val moveX = (sin * reducedX).toFloat()

                drawLine(
                    Color.Gray,
                    Offset(centerX - radius - moveX, centerY + movingOffset),
                    Offset(centerX + radius - moveX, centerY + movingOffset),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                    alpha = alpha
                )
                drawLine(
                    Color.Gray,
                    Offset(centerX - moveX, centerY - radius + movingOffset),
                    Offset(centerX - moveX, centerY + radius + movingOffset),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                    alpha = alpha
                )
                drawLine(
                    Color.Gray,
                    Offset(centerX - halfRadius - moveX, centerY - halfRadius + movingOffset),
                    Offset(centerX + halfRadius - moveX, centerY + halfRadius + movingOffset),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                    alpha = alpha
                )
                drawLine(
                    Color.Gray,
                    Offset(centerX + halfRadius - moveX, centerY - halfRadius + movingOffset),
                    Offset(centerX - halfRadius - moveX, centerY + halfRadius + movingOffset),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                    alpha = alpha
                )
            }

            drawSnowFlake(3 * fivePercent, 2 * fivePercent, fivePercent)
            drawSnowFlake(8 * fivePercent, 3 * fivePercent, fivePercent)
            drawSnowFlake(13 * fivePercent, 2 * fivePercent, fivePercent)
            drawSnowFlake(18 * fivePercent, 3 * fivePercent, fivePercent)
        }
    }
}

@Composable
@Preview
fun WeatherRaining(
    modifier: Modifier = Modifier,
    width: Int = 100,
    height: Int = 200,
    content: @Composable () -> Unit = {
        WeatherSunny(size = 80)
        WeatherCloud(size = 100)
        WeatherRain(size = 100)
    }
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { it.measure(constraints) }
        val halfH = height.div(2)

        layout(
            width = width.dp.toPx().roundToInt(),
            height = height.dp.toPx().roundToInt()
        ) {
            placeables[0].place(0, 0)
            placeables[1].place(0, 0)
            placeables[2].place(0, halfH.dp.toPx().roundToInt())
        }
    }
}

@Composable
@Preview
fun WeatherSnowing(
    modifier: Modifier = Modifier,
    width: Int = 100,
    height: Int = 200,
    content: @Composable () -> Unit = {
        WeatherSunny(size = 80)
        WeatherCloud(size = 100)
        WeatherSnow(size = 100)
    }
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { it.measure(constraints) }
        val halfH = height.div(2)

        layout(
            width = width.dp.toPx().roundToInt(),
            height = height.dp.toPx().roundToInt()
        ) {
            placeables[0].place(0, 0)
            placeables[1].place(0, 0)
            placeables[2].place(0, halfH.dp.toPx().roundToInt())
        }
    }
}

@Composable
@Preview
fun WeatherSnowingNight(
    modifier: Modifier = Modifier,
    width: Int = 100,
    height: Int = 200,
    content: @Composable () -> Unit = {
        WeatherCloud(size = 100)
        WeatherSnow(size = 100)
    }
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { it.measure(constraints) }
        val halfH = height.div(2)

        layout(
            width = width.dp.toPx().roundToInt(),
            height = height.dp.toPx().roundToInt()
        ) {
            placeables[0].place(0, 0)
            placeables[1].place(0, halfH.dp.toPx().roundToInt())
        }
    }
}
