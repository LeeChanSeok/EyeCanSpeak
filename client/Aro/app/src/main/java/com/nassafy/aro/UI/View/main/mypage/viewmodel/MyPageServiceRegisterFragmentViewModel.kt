package com.nassafy.aro.ui.view.main.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.nassafy.aro.domain.repository.MyPageRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageServiceRegisterFragmentViewModel @Inject constructor(private val myPageRepository: MyPageRepository) :
    ViewModel() {

    var auroraService: Boolean = false
    var meteorService: Boolean = false
    val setSelectServiceNetworkResultLiveData: LiveData<NetworkResult<JsonObject>> get() = myPageRepository.setSelectServiceNetworkResultLiveData

    suspend fun selectService(auroraService: Boolean, meteorService: Boolean) {
        viewModelScope.launch {
            myPageRepository.selectService(auroraService, meteorService)
        }
    }

}