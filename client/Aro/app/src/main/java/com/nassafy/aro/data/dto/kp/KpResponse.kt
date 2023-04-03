package com.nassafy.aro.data.dto.kp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KpResponse(
    val dateTime: String,
    val kp: Double
) : Parcelable