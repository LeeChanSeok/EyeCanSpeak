package com.nassafy.aro.domain.api

import com.nassafy.aro.data.dto.UserTest
import retrofit2.Response
import retrofit2.http.POST

interface SplashApi {
    // AccessToken을 토큰을 보내서 회원정보를 반환 받음
    @POST("api/members/memberInfo")
    suspend fun postAccessTokenGetUserData(): Response<UserTest>

} // End of SplashApi
