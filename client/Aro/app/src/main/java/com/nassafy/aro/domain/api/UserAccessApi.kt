package com.nassafy.aro.domain.api

import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.TokenResponse
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.SnsLoginResposne
import com.nassafy.aro.data.dto.UserTest
import retrofit2.Response
import retrofit2.http.*


interface UserAccessApi {
    @POST("api/members/login")
    suspend fun login(
        @Body loginBody: JsonObject
    ): Response<TokenResponse> // End of login

    @POST("api/members/codecheck")
    suspend fun validateEmailAuthenticationCode(
        @Body validateEmailAuthenticationCodeBody: JsonObject
    ): Response<Unit>

    @POST("api/members/emailcheck")
    suspend fun validateEmail(
        @Body validateEmailBody: JsonObject
    ): Response<Unit>

    @GET("api/attractions/all/nations")
    suspend fun getCountryList(): Response<List<String>>

    @GET("api/stamps/signup/{nation}")
    suspend fun getPlaceList(
        @Path("nation") nation: String
    ): Response<List<PlaceItem>>

    @POST("api/members/signup")
    suspend fun join(
        @Body user: JsonObject
    ): Response<TokenResponse>

    @POST("api/members/snslogin")
    suspend fun snsLogin(
        @Body reuqestBody: JsonObject
    ): Response<SnsLoginResposne>

} // End of UserAccessApi