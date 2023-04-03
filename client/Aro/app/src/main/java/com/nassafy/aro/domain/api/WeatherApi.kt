package com.nassafy.aro.domain.api

import com.nassafy.aro.data.dto.weather.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    // "https://api.openweathermap.org/data/2.5/"

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("units") units : String,
        @Query("appid") appid : String
    ) : Response<WeatherResponse>
}