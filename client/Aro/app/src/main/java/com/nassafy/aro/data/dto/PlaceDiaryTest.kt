package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PlaceDiaryTest @JvmOverloads constructor(
    var countryName: String?,
    var placeName: String?,
    var placeExplanation: String?,
    var diaryContent: String?,
    var placeImageList: List<Int>
) : Parcelable