package com.nassafy.aro.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubAccessTokenResponse(
    @SerializedName("access_token") val access_token: String,
    val scope: String,
    @SerializedName("token_type") val token_type: String
): Parcelable