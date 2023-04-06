package com.nassafy.aro.domain.api

import retrofit2.Response
import retrofit2.http.GET

interface SplashApi {
    // AccessToken을 토큰을 보내서 회원정보를 반환 받음
    // API 19

    /*
    [Response]
    Http status
    200  OK
    400  Bad Request
     */
    @GET("api/members/autologin")
    suspend fun postAccessTokenGetUserData(): Response<Void>
} // End of SplashApi
