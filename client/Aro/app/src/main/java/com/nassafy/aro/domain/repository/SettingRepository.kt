package com.nassafy.aro.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.nassafy.aro.domain.api.SettingApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.setNetworkResult
import javax.inject.Inject

class SettingRepository @Inject constructor(private val settingApi: SettingApi) {

    private val _getAlarmOptionNetworkResultLiveData = MutableLiveData<NetworkResult<Boolean>>()
    val getAlarmOptionNetworkResultLiveData: LiveData<NetworkResult<Boolean>> get() = _getAlarmOptionNetworkResultLiveData

    private val _setAlarmOptionNetworkResultLiveData = MutableLiveData<NetworkResult<Unit>>()
    val setAlarmOptionNetworkResultLiveData: LiveData<NetworkResult<Unit>> get() = _setAlarmOptionNetworkResultLiveData

    private val _getAuroraDisplayOptionNetworkResultLiveData =
        MutableLiveData<NetworkResult<Boolean>>()
    val getAuroraDisplayOptionNetworkResultLiveData: LiveData<NetworkResult<Boolean>> get() = _getAuroraDisplayOptionNetworkResultLiveData

    private val _setAuroraDisplayOptionNetworkResultLiveData =
        MutableLiveData<NetworkResult<Unit>>()
    val setAuroraDisplayOptionNetworkResultLiveData: LiveData<NetworkResult<Unit>> get() = _setAuroraDisplayOptionNetworkResultLiveData

    private val _getCloudDisplayOptionNetworkResultLiveData =
        MutableLiveData<NetworkResult<Boolean>>()
    val getCloudDisplayOptionNetworkResultLiveData: LiveData<NetworkResult<Boolean>> get() = _getCloudDisplayOptionNetworkResultLiveData

    private val _setCloudDisplayOptionNetworkResultLiveData = MutableLiveData<NetworkResult<Unit>>()
    val setCloudDisplayOptionNetworkResultLiveData: LiveData<NetworkResult<Unit>> get() = _setCloudDisplayOptionNetworkResultLiveData

    private val _deleteAccountNetworkResultLiveData = MutableLiveData<NetworkResult<Unit>>()
    val deleteAccountNetworkResultLiveData: LiveData<NetworkResult<Unit>> get() = _deleteAccountNetworkResultLiveData

    suspend fun getAlarmOption() {
        val response = settingApi.getAlarmOption()
        _getAlarmOptionNetworkResultLiveData.setNetworkResult(response)
    } // End of getAlarmOption

    suspend fun setAlarmOption() {
        val response = settingApi.setAlarmOption()
        _setAlarmOptionNetworkResultLiveData.postValue(NetworkResult.Loading())

        try {
            when {
                response.isSuccessful -> {
                    _setAlarmOptionNetworkResultLiveData.postValue(
                        NetworkResult.Success(Unit)
                    ) // End of postValue
                } // End of response.isSuccessful
                response.errorBody() != null -> {
                    _setAlarmOptionNetworkResultLiveData.postValue(
                        NetworkResult.Error(
                            response.errorBody()!!.string()
                        )
                    )
                } // End of response.errorBody
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        }
    } // End of setAlarmOption

    suspend fun getAuroraDisplayOption() {
        val response = settingApi.getAuroraDisplayOption()
        _getAuroraDisplayOptionNetworkResultLiveData.setNetworkResult(response)
    } // End of getAuroraDisplayOption

    suspend fun setAuroraDisplayOption() {
        val response = settingApi.setAuroraDisplayOption()
        _setAuroraDisplayOptionNetworkResultLiveData.postValue(NetworkResult.Loading())

        try {
            when {
                response.isSuccessful -> {
                    _setAuroraDisplayOptionNetworkResultLiveData.postValue(
                        NetworkResult.Success(Unit)
                    ) // End of postValue
                } // End of response.isSuccessful
                response.errorBody() != null -> {
                    _setAuroraDisplayOptionNetworkResultLiveData.postValue(
                        NetworkResult.Error(
                            response.errorBody()!!.string()
                        )
                    )
                } // End of response.errorBody
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        }
    } // End of setAuroraDisplayOption

    suspend fun getCloudDisplayOption() {
        val response = settingApi.getCloudDisplayOption()
        _getCloudDisplayOptionNetworkResultLiveData.setNetworkResult(response)
    } // End of getAuroraDisplayOption

    suspend fun setCloudDisplayOption() {
        val response = settingApi.setCloudDisplayOption()
        _setCloudDisplayOptionNetworkResultLiveData.postValue(NetworkResult.Loading())

        try {
            when {
                response.isSuccessful -> {
                    _setCloudDisplayOptionNetworkResultLiveData.postValue(
                        NetworkResult.Success(Unit)
                    ) // End of postValue
                } // End of response.isSuccessful
                response.errorBody() != null -> {
                    _setCloudDisplayOptionNetworkResultLiveData.postValue(
                        NetworkResult.Error(
                            response.errorBody()!!.string()
                        )
                    )
                } // End of response.errorBody
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        }
    } // End of setCloudDisplayOption

    suspend fun deleteAccount(email: String) {
        val response = settingApi.deleteAccount(JsonObject().apply {
            addProperty("email", email)
        })
        _deleteAccountNetworkResultLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _deleteAccountNetworkResultLiveData.postValue(
                        NetworkResult.Success(Unit)
                    ) // End of postValue
                } // End of response.isSuccessful
                response.errorBody() != null -> {
                    _deleteAccountNetworkResultLiveData.postValue(
                        NetworkResult.Error(
                            response.errorBody()!!.string()
                        )
                    )
                } // End of response.errorBody
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        }
    } // End of deleteAccount

} // End of SettingRepository