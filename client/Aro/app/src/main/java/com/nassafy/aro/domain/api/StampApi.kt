package com.nassafy.aro.domain.api

import com.nassafy.aro.data.dto.StampHomeItem
import com.nassafy.aro.data.dto.UserStampPlace
import retrofit2.Response
import retrofit2.http.*


interface StampApi {

    // ==================================== 전체 국가 리스트 가져오기 ====================================
    // API 20
    @GET("api/attractions/all/nations")
    suspend fun getAllNationList(): Response<List<String>>

    // ==================================== 국가 이름을 기반으로 유저에 관련된 해당 국가 데이터를 가져옴 ====================================
    // https://j8d106.p.ssafy.io/api/attractions/stamps/collections/{국가명}
    // API 26
    @GET("api/attractions/stamps/collections/{countryName}")
    suspend fun getUserStampDataGroupByCountry(
        @Path("countryName") countryName: String
    ): Response<StampHomeItem>

    // ========================================== 국가별 명소 & 유저 데이터 가져오기 ==========================================
    // api/stamps/detail/all/<String: 국가명>
    // API 37
    @GET("api/stamps/detail/all/{countryName}")
    suspend fun getUserPlaceDataGroupByCountry(
        @Path("countryName") countryName: String
    ): Response<List<UserStampPlace>>

} // End of StampApi
