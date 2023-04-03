package com.nassafy.aro.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nassafy.aro.BuildConfig
import com.nassafy.aro.data.dto.weather.WeatherForecast
import com.nassafy.aro.data.dto.weather.WeatherResponse
import com.nassafy.aro.domain.api.AuroraApi
import com.nassafy.aro.domain.api.WeatherApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.di.HeaderInterceptorApi
import com.nassafy.aro.util.di.WithoutHeaderInterceptorApi
import javax.inject.Inject

private const val TAG = "WeatherRepository_sdr"
class WeatherRepository @Inject constructor(
    @WithoutHeaderInterceptorApi private val weatherApi: WeatherApi,
){
    private val _weatherForecastLiveData = MutableLiveData<NetworkResult<WeatherForecast>>()
    val weatherForecastLiveData : LiveData<NetworkResult<WeatherForecast>>
        get () = _weatherForecastLiveData

    private val _weatherCurrentLiveData = MutableLiveData<NetworkResult<WeatherResponse>>()
    val weatherCurrentLiveData : LiveData<NetworkResult<WeatherResponse>>
        get () = _weatherCurrentLiveData

    suspend fun getCurrentWeather(lat: String, lon: String) {
        val response = weatherApi.getCurrentWeather(lat, lon, "metric", BuildConfig.WEATHER_API_KEY)
        _weatherCurrentLiveData.postValue(NetworkResult.Loading())
        when {
            response.isSuccessful && response.body() != null -> {
                _weatherCurrentLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            response.errorBody() != null -> {
                _weatherCurrentLiveData.postValue(NetworkResult.Error(response.errorBody().toString()))
            }
            else -> {
                _weatherCurrentLiveData.postValue(NetworkResult.Error(response.headers().toString()))
            }
        }
    }
}