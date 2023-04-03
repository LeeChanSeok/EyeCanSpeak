package com.nassafy.aro.data.dto.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherResponse>,
    val message: Int
) : Parcelable