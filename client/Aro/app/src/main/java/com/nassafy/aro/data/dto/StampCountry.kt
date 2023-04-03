package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StampCountry @JvmOverloads constructor(
    var countryName : String,
    var countryMapImage : String

) : Parcelable {
}