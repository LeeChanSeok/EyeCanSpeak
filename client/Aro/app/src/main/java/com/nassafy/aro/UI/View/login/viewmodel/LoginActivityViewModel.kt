package com.nassafy.aro.ui.view.login.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.domain.repository.UserAccessRepository
import com.nassafy.aro.ui.view.ServiceViewModel
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(
    private val userAccessRepository: UserAccessRepository
) : ServiceViewModel() {

    var isTriedGithubLogin = false
    var githubCode = ""

    var providerType: String = "LOCAL"
    var email: String = ""
    var password: String? = null
    var nickname: String = ""
    override var isAuroraServiceSelected: Boolean = false
    override var isMeteorServiceSelected: Boolean = false
    val selectedAuroraPlaces: LiveData<MutableList<PlaceItem>> get() = userAccessRepository.selectedAuraraPlaceListLiveData
    val selectedMeteorPlaces: LiveData<MutableList<PlaceItem>> get() = userAccessRepository.selectedMeteorPlaceListLiveData

    val countryListLiveData: LiveData<NetworkResult<List<String>>>
        get() = userAccessRepository.countryListLiveData
    val placeListLiveData: LiveData<NetworkResult<List<PlaceItem>>>
        get() = userAccessRepository.placeListLiveData

    // 임시
    val placeListData = mutableStateListOf<PlaceItem>()
    val selectedAuroraPlaceList = mutableStateListOf<PlaceItem>()

    fun getCountryList() {
        viewModelScope.launch {
            userAccessRepository.getCountryList()
        }
    }

    fun getPlaceList(nation: String) {
        viewModelScope.launch {
            userAccessRepository.getPlaceList(nation)
        }
    }


    override fun selectAuroraPlace(place: PlaceItem) {
        viewModelScope.launch {
            userAccessRepository.selectAuroraPlace(place)
        }
    }

    override fun unSelectAuroraPlace(place: PlaceItem) {
        viewModelScope.launch {
            userAccessRepository.unSelectAuroraPlace(place)
        }
    }

    fun clearSelectedService() {
        userAccessRepository
    }

    fun clearSelectedAuroraPlaceList() {
        userAccessRepository.clearSelectedAuroraPlaceList()
    }

    fun clearSelectedMeteorPlaceList() {
        userAccessRepository.clearSelectedMeteorPlaceList()
    }

}