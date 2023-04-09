package com.nassafy.aro.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nassafy.aro.data.dto.kp.MyMeteor
import com.nassafy.aro.domain.api.MeteorShowerApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.di.HeaderInterceptorApi
import javax.inject.Inject

class MeteorShowerRepository @Inject constructor(
    @HeaderInterceptorApi private val meteorShowerApi: MeteorShowerApi
) {

    // ============================================= 관심 국가 유성우 데이터 가져오기 =============================================
    private val _getMyMeteorShowerResponseLiveData = MutableLiveData<NetworkResult<MyMeteor>>()
    val getMyMeteorShowerResponseLiveData: LiveData<NetworkResult<MyMeteor>>
        get() = _getMyMeteorShowerResponseLiveData

    suspend fun getMyMeteorShower() {
        val response = meteorShowerApi.getMyMeteorShower()

        _getMyMeteorShowerResponseLiveData.postValue(NetworkResult.Loading())

        when {
            response.isSuccessful && response.body() != null -> {
                _getMyMeteorShowerResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            response.errorBody() != null -> {
                _getMyMeteorShowerResponseLiveData.postValue(
                    NetworkResult.Error(
                        response.errorBody()!!.string()
                    )
                )
            }
        }

    } // End of getMyMeteorShower

} // End of MeteorShowerRepository class
