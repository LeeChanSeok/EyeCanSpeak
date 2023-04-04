package com.nassafy.aro.ui.view.main.mypage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.MeteorCountry
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.domain.repository.MyPageRepository
import com.nassafy.aro.ui.view.ServiceViewModel
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.setNetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.function.Predicate
import javax.inject.Inject

@HiltViewModel
class MyPageFavoriteRegisterFragmentViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
) : ServiceViewModel() {
    val placeList = mutableStateListOf<PlaceItem>()
    val meteorCountryList = mutableStateListOf<MeteorCountry>()
    val favoriteAuroraPlaceList = mutableStateListOf<PlaceItem>()

    private val _favoriteMeteorCountry = MutableLiveData<MeteorCountry?>(null)
    val favoriteMeteorCountry: LiveData<MeteorCountry?>
        get() = _favoriteMeteorCountry
    val countryListNetworkResultLiveData
        get() = myPageRepository.countryListNetworkResultLiveData
    val favoriteListNetworkResultLiveData
        get() = myPageRepository.favoriteListNetworkResultLiveData
    val meteorCountryListNetworkResultLiveData
        get() = myPageRepository.meteorCountryListNetworkResultLiveData
    val placeListNetworkResultLiveData: LiveData<NetworkResult<List<PlaceItem>>>
        get() = myPageRepository.placeListNetworkResultLiveData
    val postFavoriteListNetworkResultLiveData: LiveData<NetworkResult<Unit>>
        get() = myPageRepository.postFavoriteListNetworkResultLiveData
    val setSelectServiceNetworkResultLiveData: LiveData<NetworkResult<JsonObject>>
        get() = myPageRepository.setSelectServiceNetworkResultLiveData

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

    fun getMeteorCountryList() {
        viewModelScope.launch {
            myPageRepository.getMeteorCountryList()
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

    override fun selectMeteorCountry(country: MeteorCountry) {
        _favoriteMeteorCountry.value = country
    }

    override fun unSelectMeteorCountry() {
        _favoriteMeteorCountry.value = null
    }


}