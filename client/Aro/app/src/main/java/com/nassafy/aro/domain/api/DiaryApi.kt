package com.nassafy.aro.domain.api


import com.nassafy.aro.data.dto.Diary
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

private const val TAG = "DiaryApi_싸피"

interface DiaryApi {
    // https://j8d106.p.ssafy.io/api/stamps/diary/1


    // 명소별 일기 가져오기
    // https://j8d106.p.ssafy.io/api/stamps/diary/1
    @JvmSuppressWildcards
    @GET("/api/stamps/diary/{placeId}")
    suspend fun getPlaceUserDiary(
        @Path("placeId") placeId: Long,
    ): Response<Diary>  // End of getPlaceUserDiary


    // 일지 개별 스템프 작성 및 수정
    // https://j8d106.p.ssafy.io/api/stamps/diary/1
    @Multipart
    @JvmSuppressWildcards
    @POST("/api/stamps/diary/{placeId}")
    suspend fun createStampDiary(
        @Path("placeId") placeId: Long,
        @Part newImageList: List<MultipartBody.Part?>?,
        @PartMap() requestHashMap: HashMap<String, RequestBody>,
    ): Response<Void>
} // End of DiaryApi interface
