package com.nassafy.aro.ui.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.domain.repository.MainRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    var nickname: String = ""
    var email: String = ""
    var alarmOption: Boolean = false
    var auroraDisplayOption: Boolean = false

    val userInfo: LiveData<NetworkResult<UserTest>> get() = mainRepository.userInfo
    val getAlarmOptionNetworkResultLiveData get() = mainRepository.getAlarmOptionNetworkResultLiveData
    val getAuroraOptionNetworkResultLiveData get() = mainRepository.getAuroraDisplayOptionNetworkResultLiveData

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

} // End of MainActivityViewModel class
