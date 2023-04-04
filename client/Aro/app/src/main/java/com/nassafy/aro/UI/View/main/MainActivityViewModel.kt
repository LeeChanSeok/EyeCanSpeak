package com.nassafy.aro.ui.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.domain.repository.MainRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel_sdr"

@HiltViewModel

class MainActivityViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    var nickname: String = ""
    var email: String = ""
    var alarmOption: Boolean = false
    var auroraDisplayOption: Boolean = false
    var cloudDisplayOption: Boolean = false
    var auroraServiceEnabled: Boolean = false
    var meteorShowerServiceEnabled: Boolean = false

    val logoutNetworkResultLiveData get() = mainRepository.logoutNetworkResultLiveData
    val userInfo: LiveData<NetworkResult<UserTest>> get() = mainRepository.userInfo
    val getAlarmOptionNetworkResultLiveData get() = mainRepository.getAlarmOptionNetworkResultLiveData
    val getAuroraOptionNetworkResultLiveData get() = mainRepository.getAuroraDisplayOptionNetworkResultLiveData
    val getCloudOptionNetworkResultLiveData get() = mainRepository.getCloudDisplayOptionNetworkResultLiveData

    fun logout(grantType: String, accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            mainRepository.logout(grantType, accessToken, refreshToken)
        }
    }

    fun getUserInfo(fcmToken: String) {
        viewModelScope.launch {
            mainRepository.getUserInfo(fcmToken)
        }
    } // End of getUserInfo

    fun getAlarmOption() {
        viewModelScope.launch {
            mainRepository.getAlarmOption()
        }
    } // End of getAlarmOption

    fun getAuroraDisplayOption() {
        viewModelScope.launch {
            mainRepository.getAuroraDisplayOption()
        }
    } // End of getAuroraDisplayOption

    fun getCloudDisplayOption() {
        viewModelScope.launch {
            mainRepository.getCloudDisplayOption()
        }
    } // End of getAuroraDisplayOption

} // End of MainActivityViewModel class
