package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MeteorCountry @JvmOverloads constructor(
    val countryId: Long,
    val countryEmoji: String,
    val countryName: String,
    val interest: Boolean = false
) : Parcelable
