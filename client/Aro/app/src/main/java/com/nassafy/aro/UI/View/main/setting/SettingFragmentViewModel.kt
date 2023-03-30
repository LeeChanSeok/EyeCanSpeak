package com.nassafy.aro.ui.view.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.domain.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingFragmentViewModel @Inject constructor(private val settingRepository: SettingRepository ): ViewModel(){
    val getAlarmOptionNetworkResultLiveData get() = settingRepository.getAlarmOptionNetworkResultLiveData
    val setAlarmOptionNetworkResultLiveData get() = settingRepository.setAlarmOptionNetworkResultLiveData
    val getAuroraOptionNetworkResultLiveData get() = settingRepository.getAuroraDisplayOptionNetworkResultLiveData
    val setAuroraOptionNetworkResultLiveData get() = settingRepository.setAuroraDisplayOptionNetworkResultLiveData
    val deleteAccountNetworkResultLiveData get() = settingRepository.deleteAccountNetworkResultLiveData

    suspend fun getAlarmOption() {
        viewModelScope.launch {
            settingRepository.getAlarmOption()
        }
    }

    fun setAlarmOption() {
        viewModelScope.launch {
            settingRepository.setAlarmOption()
        }
    }

    suspend fun getAuroraDisplayOption() {
        viewModelScope.launch {
            settingRepository.getAuroraDisplayOption()
        }
    }

    fun setAuroraDisplayOption() {
        viewModelScope.launch {
            settingRepository.setAuroraDisplayOption()
        }
    }
    fun deleteAccount(email: String) {
        viewModelScope.launch {
            settingRepository.deleteAccount(email)
        }
    }
}