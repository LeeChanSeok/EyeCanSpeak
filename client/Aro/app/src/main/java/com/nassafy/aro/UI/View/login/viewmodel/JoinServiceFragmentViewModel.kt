package com.nassafy.aro.ui.view.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.TokenResponse
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.domain.repository.UserAccessRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinServiceFragmentViewModel @Inject constructor(
    private val userAccessRepository: UserAccessRepository
): ViewModel(){
    val userJoinNetworkResultLiveData: LiveData<NetworkResult<TokenResponse>>
        get() = userAccessRepository.userJoinNetworkResultLiveData

    fun join(user: JsonObject) {
        viewModelScope.launch {
            userAccessRepository.join(user)
        }
    }
}