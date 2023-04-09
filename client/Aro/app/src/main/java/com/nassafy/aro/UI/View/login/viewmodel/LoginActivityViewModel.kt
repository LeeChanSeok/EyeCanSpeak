package com.nassafy.aro.ui.view.login.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.MeteorCountry
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
    val selectedCountryId: LiveData<MeteorCountry?> get() = userAccessRepository.selectedMeteorCountryLiveData

    val placeListLiveData: LiveData<NetworkResult<List<PlaceItem>>>
        get() = userAccessRepository.placeListLiveData

    val placeListData = mutableStateListOf<PlaceItem>()
    val meteorCountryList = mutableStateListOf<MeteorCountry>()
    val selectedAuroraPlaceList = mutableStateListOf<PlaceItem>()

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

    override fun selectMeteorCountry(country: MeteorCountry) {
        userAccessRepository.selectMeteorCountry(country)
    }

    override fun unSelectMeteorCountry() {
        userAccessRepository.unSelectMeteorCountry()
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