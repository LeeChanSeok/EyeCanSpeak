package com.nassafy.aro.ui.view.main.stamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.domain.repository.StampRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampViewModel @Inject constructor(private val stampRepository: StampRepository) :
    ViewModel() {

    // ================================= 유저별 국가 스탬프 데이터 가져오기 =================================
    // getUserStampDataGroupByCountry

    //val getUserStampDataGroupByCountryResponseLiveData: LiveData<NetworkResult<String>>
        //get() = stampRepository.getUserStampDataGroupByCountryResponseLiveData

    suspend fun getUserStampDataGroupByCountry() {
        viewModelScope.launch {
            //stampRepository.getUserStampDataGroupByCountry()
        }
    } // End of getUserStampDataGroupByCountry
} // End of StampViewModel class
