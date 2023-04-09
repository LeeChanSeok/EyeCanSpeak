package com.nassafy.aro.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MeteorCountry @JvmOverloads constructor(
    val countryId: Long,
    val countryEmoji: String,
    @SerializedName("country") val countryName: String,
    val interest: Boolean = false
) : Parcelable
