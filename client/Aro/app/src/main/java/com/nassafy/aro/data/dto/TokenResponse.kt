package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenResponse @JvmOverloads constructor(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
): Parcelable
