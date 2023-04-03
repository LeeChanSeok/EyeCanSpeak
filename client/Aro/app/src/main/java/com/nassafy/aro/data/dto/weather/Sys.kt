package com.nassafy.aro.data.dto.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sys(
    val pod: String
) : Parcelable