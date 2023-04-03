package com.nassafy.aro.domain.api

import retrofit2.Response
import retrofit2.http.GET

interface TestApi {
    @GET("/")
    suspend fun serverCallTest(): Response<String>
} // End of TestApi inferface
