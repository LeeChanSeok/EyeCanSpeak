package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StampTest @JvmOverloads constructor(
    var colorStamp: String?,
    var certifictaion: String?
) : Parcelable