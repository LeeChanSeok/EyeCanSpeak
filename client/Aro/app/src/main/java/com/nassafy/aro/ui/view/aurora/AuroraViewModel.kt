package com.nassafy.aro.ui.view.aurora

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.kp.KpResponse
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.kp.KpWithProbs
import com.nassafy.aro.domain.repository.AuroraRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

private const val TAG = "AuroraViewModel_sdr"

@HiltViewModel
class AuroraViewModel @Inject constructor(
    private val auroraRepository: AuroraRepository,
) : ViewModel() {

    val placeItemListLiveData: LiveData<NetworkResult<List<PlaceItem>>>
        get() = auroraRepository.placeItemListLiveData
    fun getPlaceItemList(dateString: String, hour: Int) {
        viewModelScope.launch {
            auroraRepository.getPlaceItemList(dateString, hour)
        }
    } // End of getPlaceItemList
    
    val currentKpIndexLiveData: LiveData<NetworkResult<KpResponse>>
        get() = auroraRepository.kpCurrentLiveData
    fun getCurrentKpIndex(dateString: String, hour: Int) {
        viewModelScope.launch {
            auroraRepository.getCurrentKpIndex(dateString, hour)
        }
    } // End

    val kpAndProbsLiveData: LiveData<NetworkResult<KpWithProbs>>
        get() = auroraRepository.kpAndProbsLiveData
    fun getKpAndProbsLiveData(dateString: String, hour: Int) {
        viewModelScope.launch {
            auroraRepository.getKpAndProbsLiveData(dateString, hour)
        }
    }

} // End of AuroraViewModel