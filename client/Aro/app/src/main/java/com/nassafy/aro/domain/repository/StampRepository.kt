package com.nassafy.aro.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nassafy.aro.data.dto.StampHomeItem
import com.nassafy.aro.data.dto.UserStampPlace
import com.nassafy.aro.domain.api.StampApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.di.HeaderInterceptorApi
import com.nassafy.aro.util.di.WithoutHeaderInterceptorApi
import javax.inject.Inject

// provideStampApi
private const val TAG = "StampRepository_싸피"

class StampRepository @Inject constructor(
    @WithoutHeaderInterceptorApi private val stampApi: StampApi,
    @HeaderInterceptorApi private val stampHeaderApi: StampApi
) {

    // ==================================== 전체 국가 리스트 가져오기 ====================================
    private val _getAllNationListResponseLiveData = MutableLiveData<NetworkResult<List<String>>>()
    val getAllNationListResponseLiveData: LiveData<NetworkResult<List<String>>>
        get() = _getAllNationListResponseLiveData

    suspend fun getAllNationList() {
        val response = stampApi.getAllNationList()

        _getAllNationListResponseLiveData.postValue(NetworkResult.Loading())

        when {
            response.isSuccessful && response.body() != null -> {
                _getAllNationListResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            response.errorBody() != null -> {
                _getAllNationListResponseLiveData.postValue(
                    NetworkResult.Error(
                        response.errorBody()!!.string()
                    )
                )
            }
        }
    } // End of getAllNationList

    // ================================= 유저별 국가 스탬프 데이터 가져오기 =================================
    private val _getUserStampDataGroupByCountryResponseLiveData =
        MutableLiveData<NetworkResult<StampHomeItem>>()
    val getUserStampDataGroupByCountryResponseLiveData: LiveData<NetworkResult<StampHomeItem>>
        get() = _getUserStampDataGroupByCountryResponseLiveData

    suspend fun getUserStampDataGroupByCountry(countryName: String) {
        val response = stampHeaderApi.getUserStampDataGroupByCountry(countryName)

        _getUserStampDataGroupByCountryResponseLiveData.postValue(NetworkResult.Loading())

        when {
            response.isSuccessful && response.body() != null -> {
                _getUserStampDataGroupByCountryResponseLiveData.postValue(
                    NetworkResult.Success(
                        response.body()!!
                    )
                )
            }
            response.errorBody() != null -> {
                _getUserStampDataGroupByCountryResponseLiveData.postValue(
                    NetworkResult.Error(
                        response.errorBody()!!.string()
                    )
                )
            }
        }
    }  // End of getUserStampDataGroupByCountry

    // ========================================== 국가별 명소 & 유저 데이터 가져오기 ==========================================
    private val _getUserPlaceDataGroupByCountryResponseLiveData =
        MutableLiveData<NetworkResult<List<UserStampPlace>>>()
    val getUserPlaceDataGroupByCountryResponseLiveData: LiveData<NetworkResult<List<UserStampPlace>>>
        get() = _getUserPlaceDataGroupByCountryResponseLiveData

    suspend fun getUserPlaceDataGroupByCountry(countryName: String) {
        val response = stampHeaderApi.getUserPlaceDataGroupByCountry(countryName.toString())

        _getUserPlaceDataGroupByCountryResponseLiveData.postValue(NetworkResult.Loading())

        when {
            response.isSuccessful && response.body() != null -> {
                _getUserPlaceDataGroupByCountryResponseLiveData.postValue(
                    NetworkResult.Success(
                        response.body()!!
                    )
                )
            }
            response.errorBody() != null -> {
                _getUserPlaceDataGroupByCountryResponseLiveData.postValue(
                    NetworkResult.Error(
                        response.errorBody()!!.string()
                    )
                )
            }
        }

    } // End of getUserPlaceDataGroupByCountry
} // End of StampRepository class
