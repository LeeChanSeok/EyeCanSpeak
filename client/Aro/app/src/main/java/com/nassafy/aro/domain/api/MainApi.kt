package com.nassafy.aro.domain.api

import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.UserTest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainApi {

    @POST("api/members/logout")
    suspend fun logout(@Body requestBody: JsonObject) : Response<Unit>

    @POST("api/members/memberInfo")
    suspend fun getUserInfo(
        @Body requestBody: JsonObject
    ): Response<UserTest>

    @GET("api/members/alarm")
    suspend fun getAlarmOption(): Response<Boolean>

    @GET("api/members/auroraDisplay")
    suspend fun getAuroraDisplayOption(): Response<Boolean>

} // End of MainApi interface
