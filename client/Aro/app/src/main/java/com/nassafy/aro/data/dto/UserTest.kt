package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTest @JvmOverloads constructor(
    val auroraPlaces: List<Int>,
    val auroraService: Boolean,
    val email: String,
    val countryId: Long,
    val meteorService: Boolean,
    val nickname: String,
    val password: String?,
    val alarm: Boolean = true
): Parcelable