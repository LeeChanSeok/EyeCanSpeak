package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StampHomeItem @JvmOverloads constructor(
    var mapImage: String,
    var mapStampsItem: List<MapStampItem>,
) : Parcelable