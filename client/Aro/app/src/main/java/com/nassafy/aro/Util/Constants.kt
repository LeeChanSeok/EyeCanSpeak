package com.nassafy.aro.util

import android.net.Uri
import com.nassafy.aro.BuildConfig

const val BASE_URL = ""
const val SERVER_URL = "https://j8d106.p.ssafy.io/"
const val WEATHER_URL = "https://api.openweathermap.org/data/2.5/"
val githubLoginUri = Uri.Builder().scheme("https").authority("github.com")
    .appendPath("login")
    .appendPath("oauth")
    .appendPath("authorize")
    .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
//    .appendQueryParameter("scope", "user:email")
    .build()

const val githubBaseUrl = "https://github.com/"
