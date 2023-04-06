package com.nassafy.aro.ui.view.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.domain.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingFragmentViewModel @Inject constructor(private val settingRepository: SettingRepository) :
    ViewModel() {
    val getAlarmOptionNetworkResultLiveData get() = settingRepository.getAlarmOptionNetworkResultLiveData
    val setAlarmOptionNetworkResultLiveData get() = settingRepository.setAlarmOptionNetworkResultLiveData
    val getAuroraOptionNetworkResultLiveData get() = settingRepository.getAuroraDisplayOptionNetworkResultLiveData
    val setAuroraOptionNetworkResultLiveData get() = settingRepository.setAuroraDisplayOptionNetworkResultLiveData
    val getCloudOptionNetworkResultLiveData get() = settingRepository.getCloudDisplayOptionNetworkResultLiveData
    val setCloudOptionNetworkResultLiveData get() = settingRepository.setCloudDisplayOptionNetworkResultLiveData
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
            var result: Deferred<Int> = async {
                settingRepository.setAuroraDisplayOption()
                1
            }
            result.await()
        }
    }

    suspend fun getCloudDisplayOption() {
        viewModelScope.launch {
            settingRepository.getCloudDisplayOption()
        }
    }

    fun setCloudDisplayOption() {
        viewModelScope.launch {
            var result: Deferred<Int> = async {
                settingRepository.setCloudDisplayOption()
                1
            }
            result.await()
        }
    }

    fun deleteAccount(email: String) {
        viewModelScope.launch {
            settingRepository.deleteAccount(email)
        }
    }
}