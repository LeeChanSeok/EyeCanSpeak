package com.nassafy.aro.data.dto.kp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Probability(
    val attractionName: String,
    val prob: Int,
    val weather: String
) : Parcelable