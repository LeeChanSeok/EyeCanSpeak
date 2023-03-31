package com.nassafy.aro.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.domain.api.MainApi
import com.nassafy.aro.util.NetworkResult
import javax.inject.Inject

class MainRepository @Inject constructor(private val mainApi: MainApi) {

    private val _userInfo = MutableLiveData<NetworkResult<UserTest>>()
    val userInfo: LiveData<NetworkResult<UserTest>> get() = _userInfo

    private val _getAlarmOptionNetworkResultLiveData = MutableLiveData<NetworkResult<Boolean>>()
    val getAlarmOptionNetworkResultLiveData: LiveData<NetworkResult<Boolean>> get() = _getAlarmOptionNetworkResultLiveData

    private val _getAuroraDisplayOptionNetworkResultLiveData = MutableLiveData<NetworkResult<Boolean>>()
    val getAuroraDisplayOptionNetworkResultLiveData: LiveData<NetworkResult<Boolean>> get() = _getAuroraDisplayOptionNetworkResultLiveData

    suspend fun getUserInfo(fcmToken: String) {
        Log.d("ssafy/getInfo", fcmToken)
        val response = mainApi.getUserInfo(JsonObject().apply {
            addProperty("fcmToken", fcmToken)
        })
        _userInfo.postValue(NetworkResult.Loading())

        try {
            when {
                response.isSuccessful -> {
                    _userInfo.postValue(
                        NetworkResult.Success(
                            response.body()!!
                        )
                    ) // End of postValue
                } // End of response.isSuccessful
                response.errorBody() != null -> {
                    _userInfo.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                } // End of response.errorBody
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        }
    } // End of getUserInfo

    suspend fun getAlarmOption() {
        val response = mainApi.getAlarmOption()
        _getAlarmOptionNetworkResultLiveData.postValue(NetworkResult.Loading())

        try {
            when {
                response.isSuccessful -> {
                    _getAlarmOptionNetworkResultLiveData.postValue(
                        NetworkResult.Success(response.body()!!)
                    ) // End of postValue
                } // End of response.isSuccessful
                response.errorBody() != null -> {
                    _getAlarmOptionNetworkResultLiveData.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                } // End of response.errorBody
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        }
    } // End of getAlarmOption

    suspend fun getAuroraDisplayOption() {
        val response = mainApi.getAuroraDisplayOption()
        _getAuroraDisplayOptionNetworkResultLiveData.postValue(NetworkResult.Loading())

        try {
            when {
                response.isSuccessful -> {
                    _getAuroraDisplayOptionNetworkResultLiveData.postValue(
                        NetworkResult.Success(response.body()!!)
                    ) // End of postValue
                } // End of response.isSuccessful
                response.errorBody() != null -> {
                    _getAuroraDisplayOptionNetworkResultLiveData.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                } // End of response.errorBody
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        }
    } // End of getAuroraDisplayOption

} // End of MainRepository class
