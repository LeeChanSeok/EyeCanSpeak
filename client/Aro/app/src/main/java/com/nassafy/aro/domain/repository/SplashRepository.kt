package com.nassafy.aro.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nassafy.aro.data.dto.UserTest
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
        MutableLiveData<NetworkResult<UserTest>>()
    val postAccessTokenGetUserDataResponseLiveData: LiveData<NetworkResult<UserTest>>
        get() = _postAccessTokenGetUserDataResponseLiveData

    suspend fun postAccessTokenGetUserData() {
        val response = splashHeaderApi.postAccessTokenGetUserData()
        Log.d(TAG, "postAccessTokenGetUserData: ${response}")
        Log.d(TAG, "postAccessTokenGetUserData: ${response.body()}")
        Log.d(TAG, "postAccessTokenGetUserData: ${response.code()}")


        _postAccessTokenGetUserDataResponseLiveData.postValue(NetworkResult.Loading())

        when {
            response.isSuccessful && response.body() != null -> {
                _postAccessTokenGetUserDataResponseLiveData.postValue(
                    NetworkResult.Success(
                        response.body()!!
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
