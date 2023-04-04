package com.nassafy.aro.ui.view.main.mypage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.MeteorCountry
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
    private val _favoriteMeteorCountry = MutableLiveData<MeteorCountry?>()
    val favoriteMeteorCountry: LiveData<MeteorCountry?> get() = _favoriteMeteorCountry

    val nicknameLiveData get() = myPageRepository.nicknameLiveData
    val favoriteListNetworkResultLiveData get() = myPageRepository.favoriteListNetworkResultLiveData
    val meteorCountryListNetworkResultLiveData get() = myPageRepository.meteorCountryListNetworkResultLiveData
    val getSelectedServiceNetworResultLiveData get() = myPageRepository.getSelectedServiceNetworkResultLiveData
    val deleteFavoriteNetworkResultLiveData get() = myPageRepository.deleteFavoriteNetworkResultLiveData
    val clearFavoriteMeteorCountryNetworkResultLiveData get() = myPageRepository.postFavoriteMeteorCountryNetworkResultLiveData

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

    fun getMeteorCountryList() {
        viewModelScope.launch {
            myPageRepository.getMeteorCountryList()
        }
    }

    fun setFavoriteMeteorCountry(meteorCountry: MeteorCountry?) {
        _favoriteMeteorCountry.value = meteorCountry
    }

    fun deleteFavorite(interestId: Long) {
        viewModelScope.launch {
            myPageRepository.deleteFavorite(interestId)
        }
    }

    fun clearFavoriteMeteorCountry() {
        viewModelScope.launch {
            myPageRepository.postFavoriteMeteorCountry(null)
        }
    }

}