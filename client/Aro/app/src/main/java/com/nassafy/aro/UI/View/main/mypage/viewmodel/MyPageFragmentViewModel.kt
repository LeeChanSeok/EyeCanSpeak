package com.nassafy.aro.ui.view.main.mypage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageFragmentViewModel @Inject constructor(private val myPageRepository: MyPageRepository) : ViewModel() {

    var auroraService = false
    var meteorService = false

    val favoriteAuroraPlaceList = mutableStateListOf<PlaceItem>()
    val favoriteMeteorPlaceList = mutableStateListOf<PlaceItem>()

    val nicknameLiveData get() = myPageRepository.nicknameLiveData
    val favoriteListNetworkResultLiveData get() = myPageRepository.favoriteListNetworkResultLiveData
    val getSelectedServiceNetworResultLiveData get() = myPageRepository.getSelectedServiceNetworkResultLiveData
    val deleteFavoriteNetworkResultLiveData get() = myPageRepository.deleteFavoriteNetworkResultLiveData

    suspend fun changeNickname(nickname: String) {
        viewModelScope.launch {
            myPageRepository.changeNickname(nickname = nickname)
        } // End of viewModelScope.launch
    } // End of changeNickname

    suspend fun getSelectedServiceList() {
        viewModelScope.launch {
            myPageRepository.getSelectedServiceList()
        } // End of viewModelScope.launch
    } // End of getSelectedServiceList

    suspend fun getFavoriteList() {
        viewModelScope.launch {
            myPageRepository.getFavoriteList()
        } // End of viewModelScope.launch
    } // End of getFavoriteList

    fun deleteFavorite(interestId: Long) {
        viewModelScope.launch {
            myPageRepository.deleteFavorite(interestId)
        }
    }

}