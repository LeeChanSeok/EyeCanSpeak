package com.nassafy.aro.domain.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ValidateApi {

    // 이미지 오로라 플라스크로 확인하기
    @Multipart
    @POST("/flask/validate")
    suspend fun postImageValidate(
        @Part validateImage: MultipartBody.Part?
    ): Response<Void>
} // End of ValidateApi interface