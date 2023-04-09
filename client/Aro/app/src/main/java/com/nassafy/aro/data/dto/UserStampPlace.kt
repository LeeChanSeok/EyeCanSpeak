package com.nassafy.aro.data.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
data class UserStampPlace @JvmOverloads constructor(
    var attractionId: Long? = 0L,
    var attractionName: String? = "",
    var description: String? = "",
    var certification: Boolean? = false,
    var stamp: String? = "",
    var auth: String? = "",
    var certificationDate: String? = "",
    var attractionOriginalName: String? = ""
) : Parcelable {} // End of UserStampPlace
