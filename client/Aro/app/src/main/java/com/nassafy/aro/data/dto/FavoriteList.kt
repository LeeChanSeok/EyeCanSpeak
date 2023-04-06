package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteList @JvmOverloads constructor (
    val attractionInterestOrNotDTOList: List<PlaceItem>,
    val meteorInterestOrNotDTO: String
) : Parcelable