package com.nassafy.aro.ui.view.login.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.domain.repository.SplashRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val splashRepository: SplashRepository) :
    ViewModel() {

    // ====================================  Header에 AccessToken을 토큰만 담아서 유저 정보를 가져온다. ====================================
    val postAccessTokenGetUserDataResponseLiveData: LiveData<NetworkResult<UserTest>>
        get() = splashRepository.postAccessTokenGetUserDataResponseLiveData

    suspend fun postAccessTokenGetUserData() {
        viewModelScope.launch {
            splashRepository.postAccessTokenGetUserData()
        }
    } // End of postAccessTokenGetUserData
} // End of SplashViewModel class
