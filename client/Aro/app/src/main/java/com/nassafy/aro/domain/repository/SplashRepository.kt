package com.nassafy.aro.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nassafy.aro.domain.api.SplashApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.di.HeaderInterceptorApi
import com.nassafy.aro.util.di.WithoutHeaderInterceptorApi
import javax.inject.Inject

private const val TAG = "SplashRepository_싸피"

class SplashRepository @Inject constructor(
    @WithoutHeaderInterceptorApi private val splashApi: SplashApi,
    @HeaderInterceptorApi private val splashHeaderApi: SplashApi
) {

    // ====================================  Header에 AccessToken을 토큰만 담아서 유저 정보를 가져온다. ====================================
    private val _postAccessTokenGetUserDataResponseLiveData =
        MutableLiveData<NetworkResult<Int>>()
    val postAccessTokenGetUserDataResponseLiveData: LiveData<NetworkResult<Int>>
        get() = _postAccessTokenGetUserDataResponseLiveData

    suspend fun postAccessTokenGetUserData() {
        val response = splashHeaderApi.postAccessTokenGetUserData()

        _postAccessTokenGetUserDataResponseLiveData.postValue(NetworkResult.Loading())

        when {
            response.isSuccessful -> {
                _postAccessTokenGetUserDataResponseLiveData.postValue(
                    NetworkResult.Success(
                        response.code()
                    )
                )
            }
            response.errorBody() != null -> {
                _postAccessTokenGetUserDataResponseLiveData.postValue(
                    NetworkResult.Error(
                        response.errorBody()!!.string()
                    )
                )
            }
        }
    } // End of postAccessTokenGetUserData
} // End of SplashRepository
