package com.nassafy.aro.domain.api

import com.nassafy.aro.data.dto.KpResponse
import com.nassafy.aro.data.dto.PlaceItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AuroraApi {
    // https://j8d106.p.ssafy.io/api/

    @GET("/api/attractions/all")
    suspend fun getAllPlaces() : Response<List<PlaceItem>>

    @GET("api/forecast/{dateString}/{hour}")
    suspend fun getCurrentKpIndex(
        @Path("dateString") dateString: String,
        @Path("hour") hour: Int
    ) : Response<KpResponse>

}