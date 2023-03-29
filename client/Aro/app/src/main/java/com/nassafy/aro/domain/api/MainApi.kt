package com.nassafy.aro.domain.api

import com.nassafy.aro.data.dto.UserTest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {

    @POST("api/members/memberInfo")
    suspend fun getUserInfo(
        @Body fcmToken: String
    ): Response<UserTest>
} // End of MainApi interface
