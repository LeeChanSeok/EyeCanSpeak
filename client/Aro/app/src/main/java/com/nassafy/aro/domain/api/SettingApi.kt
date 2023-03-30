package com.nassafy.aro.domain.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SettingApi {

    @POST("api/members/withdrawal")
    suspend fun deleteAccount(
        @Body requestBody: JsonObject
    ): Response<Unit>

    @GET("api/members/alarm")
    suspend fun getAlarmOption(): Response<Boolean>

    @POST("api/members/alarm")
    suspend fun setAlarmOption(): Response<Unit>

    @GET("api/members/auroraDisplay")
    suspend fun getAuroraDisplayOption(): Response<Boolean>

    @POST("api/members/auroraDisplay")
    suspend fun setAuroraDisplayOption(): Response<Unit>

}