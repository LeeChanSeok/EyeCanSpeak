package com.nassafy.aro.domain.api

import com.nassafy.aro.data.dto.kp.KpResponse
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.kp.KpWithProbs
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AuroraApi {
    // https://j8d106.p.ssafy.io/api/


    @GET("api/attractions/probability/{dateString}/{hour}")
    suspend fun getAllPlaces(
        @Path("dateString") dateString: String,
        @Path("hour") hour: Int
    ) : Response<List<PlaceItem>>

    @GET("api/forecast/{dateString}/{hour}")
    suspend fun getCurrentKpIndex(
        @Path("dateString") dateString: String,
        @Path("hour") hour: Int
    ) : Response<KpResponse>

    @GET("api/forecast/interest/{dateString}/{hour}")
    suspend fun getKpAndProbsLiveData(
        @Path("dateString") dateString: String,
        @Path("hour") hour: Int
    ) : Response<KpWithProbs>

}