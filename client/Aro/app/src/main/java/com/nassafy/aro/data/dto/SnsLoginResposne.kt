package com.nassafy.aro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnsLoginResposne @JvmOverloads constructor(
    val providerType: String,
    val email: String,
    val signup: Boolean
): Parcelable