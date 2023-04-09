package com.nassafy.aro.domain.api

import com.nassafy.aro.data.dto.kp.MyMeteor
import retrofit2.Response
import retrofit2.http.GET

interface MeteorShowerApi {

    // ============================================= 관심 국가 유성우 데이터 가져오기 =============================================
    // API 82
    @GET("/api/meteor")
    suspend fun getMyMeteorShower(): Response<MyMeteor> // End of getMyMeteorShower

} // End of MeteorShowerApi interfece