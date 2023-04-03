package com.nassafy.aro.data.dto.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clouds(
    val all: Int
) : Parcelable