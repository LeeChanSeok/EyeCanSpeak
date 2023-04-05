package com.nassafy.aro.domain.api

import com.nassafy.aro.data.dto.GithubAccessTokenResponse
import retrofit2.Response
import retrofit2.http.*


interface GithubApi {
    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): Response<GithubAccessTokenResponse>

} // End of GithubApi