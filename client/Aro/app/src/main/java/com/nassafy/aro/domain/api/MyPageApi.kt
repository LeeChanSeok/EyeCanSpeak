package com.nassafy.aro.domain.api

import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.FavoriteList
import com.nassafy.aro.data.dto.MeteorCountry
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.UserTest
import retrofit2.Response
import retrofit2.http.*

interface MyPageApi {
    @POST("api/members/changenickname/{nickname}")
    suspend fun changeNickname(
        @Path("nickname") nickname: String
    ): Response<Unit>

    @GET("api/members/service/all")
    suspend fun getSelectedService(): Response<UserTest>

    @GET("api/interest")
    suspend fun getFavoriteList(): Response<FavoriteList>

    @GET("api/country")
    suspend fun getMeteorCountryList(): Response<List<MeteorCountry>>

    @POST("api/interest")
    suspend fun postFavoriteList(@Body requestBody: JsonObject): Response<Unit>

    @POST("api/meteor")
    suspend fun postFavoriteMeteorCountry(@Body requestBody: JsonObject): Response<Unit>

    @POST("api/members/service")
    suspend fun selectService(@Body requestBody: JsonObject): Response<JsonObject>

    @DELETE("api/interest/{interestId}")
    suspend fun deleteFavorite(@Path("interestId") interestId: Long): Response<Unit>
}

interface WithoutHeaderMyPageApi {
    @GET("api/attractions/all/nations")
    suspend fun getCountryList(): Response<List<String>>

    @GET("api/stamps/signup/{nation}")
    suspend fun getPlaceList(
        @Path("nation") nation: String
    ): Response<List<PlaceItem>>
}