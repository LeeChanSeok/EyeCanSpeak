package com.nassafy.aro.ui.view.main.mypage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.domain.repository.MyPageRepository
import com.nassafy.aro.ui.view.ServiceViewModel
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.function.Predicate
import javax.inject.Inject

@HiltViewModel
class MyPageFavoriteRegisterFragmentViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
) : ServiceViewModel() {
    val placeList = mutableStateListOf<PlaceItem>()
    val favoriteAuroraPlaceList = mutableStateListOf<PlaceItem>()
    val favoriteMeteorPlaceList = mutableStateListOf<PlaceItem>()
    val countryListNetworkResultLiveData get() = myPageRepository.countryListNetworkResultLiveData
    val favoriteListNetworkResultLiveData get() = myPageRepository.favoriteListNetworkResultLiveData
    val placeListNetworkResultLiveData: LiveData<NetworkResult<List<PlaceItem>>>
        get() = myPageRepository.placeListNetworkResultLiveData
    val postFavoriteListNetworkResultLiveData: LiveData<NetworkResult<Unit>>
        get() = myPageRepository.postFavoriteListNetworkResultLiveData
    val setSelectServiceNetworkResultLiveData: LiveData<NetworkResult<JsonObject>> get() = myPageRepository.setSelectServiceNetworkResultLiveData

    override var isAuroraServiceSelected: Boolean = false
    override var isMeteorServiceSelected: Boolean = false

    suspend fun selectService(auroraService: Boolean, meteorService: Boolean) {
        viewModelScope.launch {
            myPageRepository.selectService(auroraService, meteorService)
        }
    }

    suspend fun getCountryList() {
        viewModelScope.launch {
            myPageRepository.getCountryList()
        }
    }

    suspend fun getPlaceList(nation: String) {
        viewModelScope.launch {
            myPageRepository.getPlaceList(nation)
        }
    }

    suspend fun getFavoriteList() {
        viewModelScope.launch {
            myPageRepository.getFavoriteList()
        }
    }

    suspend fun postFavoriteList(requestBody: JsonObject) {
        viewModelScope.launch {
            myPageRepository.postFavoriteList(requestBody)
        } // End of viewModelScope.launch
    } // End of postFavoriteList

    override fun selectAuroraPlace(placeItem: PlaceItem) {
        when (favoriteAuroraPlaceList.contains(placeItem)) {
            true -> {}
            false -> {
                favoriteAuroraPlaceList.add(placeItem)
            }
        }
    }

    override fun unSelectAuroraPlace(placeItem: PlaceItem) {
//        favoriteAuroraPlaceList.remove(placeItem)
        favoriteAuroraPlaceList.removeIf(Predicate {
            it.placeName == placeItem.placeName
        })
    }


}