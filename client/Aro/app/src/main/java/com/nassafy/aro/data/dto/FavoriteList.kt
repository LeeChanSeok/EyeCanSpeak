package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteList @JvmOverloads constructor (
    val attractionInterestOrNotDTOList: List<PlaceItem>,
//    val memteorInterestOrNotDTO: List<PlaceItem> // TODO ACTIV
    val meteorInterestOrNotDTO: String // TODO DELETE
) : Parcelable