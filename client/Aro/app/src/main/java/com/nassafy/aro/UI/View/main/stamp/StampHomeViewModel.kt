package com.nassafy.aro.ui.view.main.stamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.StampHomeItem
import com.nassafy.aro.domain.repository.StampRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampHomeViewModel @Inject constructor(private val stampRepository: StampRepository) :
    ViewModel() {

    // ==================================== 전체 국가 리스트 가져오기 ====================================
    val getAllNationListResponseLiveData: LiveData<NetworkResult<List<String>>>
        get() = stampRepository.getAllNationListResponseLiveData

    suspend fun getAllNationList() {
        viewModelScope.launch {
            stampRepository.getAllNationList()
        }
    } // End of getAllNationList

    // ================================= 유저별 국가 스탬프 데이터 가져오기 =================================
    val getUserStampDataGroupByCountryResponseLiveData: LiveData<NetworkResult<StampHomeItem>>
        get() = stampRepository.getUserStampDataGroupByCountryResponseLiveData

    suspend fun getUserStampDataGroupByCountry(countryName: String) {
        viewModelScope.launch {
            stampRepository.getUserStampDataGroupByCountry(countryName)
        }

    } // End of getUserStampDataGroupByCountry
} // End of StampHomeViewModel class
