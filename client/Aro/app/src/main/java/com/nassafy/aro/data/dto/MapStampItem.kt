package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapStampItem @JvmOverloads constructor(
    var attractionId: Int,
    var colorStamp: String,
    var certification: Boolean
) : Parcelable