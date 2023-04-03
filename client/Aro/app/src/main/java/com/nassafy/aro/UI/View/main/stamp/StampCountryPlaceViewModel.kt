package com.nassafy.aro.ui.view.main.stamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.UserStampPlace
import com.nassafy.aro.domain.repository.StampRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampCountryPlaceViewModel @Inject constructor(private val stampRepository: StampRepository) :
    ViewModel() {

    // 선택한 국가 이름 데이터
    private var _selectedCountry: String = ""
    val selectedCountry: String
        get() = _selectedCountry

    fun setSelectedCountry(selectedCountry: String) {
        _selectedCountry = selectedCountry
    } // End of setSelectedCountry

    // ========================================== 국가별 유저 명소 데이터 가져오기 ==========================================
    val getUserPlaceDataGroupByCountryResponseLiveData: LiveData<NetworkResult<List<UserStampPlace>>>
        get() = stampRepository.getUserPlaceDataGroupByCountryResponseLiveData

    suspend fun getUserPlaceDataGroupByCountry(countryName : String) {
        viewModelScope.launch {
            stampRepository.getUserPlaceDataGroupByCountry(countryName)
        }
    } // End of getUserPlaceDataGroupByCountry
} // End of StampCountryPlaceViewModel
