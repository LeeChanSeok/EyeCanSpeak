package com.nassafy.aro.ui.view.aurora

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.nassafy.aro.data.dto.kp.KpResponse
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.kp.KpWithProbs
import com.nassafy.aro.data.dto.weather.WeatherResponse
import com.nassafy.aro.domain.repository.AuroraRepository
import com.nassafy.aro.domain.repository.WeatherRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

private const val TAG = "AuroraViewModel_sdr"

@HiltViewModel
class AuroraViewModel @Inject constructor(
    private val auroraRepository: AuroraRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private var _clickedLocation = MutableLiveData<LatLng>()
    val clickedLocation: LiveData<LatLng>
        get() = _clickedLocation

    fun setClickedLocation(location: LatLng) {
        _clickedLocation.value = location
    } // End of setClickedLocation

    val placeItemListLiveData: LiveData<NetworkResult<List<PlaceItem>>>
        get() = auroraRepository.placeItemListLiveData
    fun getPlaceItemList(dateString: String, hour: Int) {
        Log.d(TAG, "getPlaceItemList: Go")
        viewModelScope.launch {
            auroraRepository.getPlaceItemList(dateString, hour)
        }
        Log.d(TAG, "getPlaceItemList: End")
    }
    
    val currentKpIndexLiveData: LiveData<NetworkResult<KpResponse>>
        get() = auroraRepository.kpCurrentLiveData
    fun getCurrentKpIndex(dateString: String, hour: Int) {
        viewModelScope.launch {
            auroraRepository.getCurrentKpIndex(dateString, hour)
        }
    }

    val kpAndProbsLiveData: LiveData<NetworkResult<KpWithProbs>>
        get() = auroraRepository.kpAndProbsLiveData
    fun getKpAndProbsLiveData(dateString: String, hour: Int) {
        viewModelScope.launch {
            auroraRepository.getKpAndProbsLiveData(dateString, hour)
        }
    }

} // End of AuroraViewModel