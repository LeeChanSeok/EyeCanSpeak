package com.nassafy.aro.ui.view.aurora

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.KpResponse
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.weather.WeatherResponse
import com.nassafy.aro.domain.repository.AuroraRepository
import com.nassafy.aro.domain.repository.WeatherRepository
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.addInfoWindow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.ln

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

    fun getPlaceItemList() {
        viewModelScope.launch {
            auroraRepository.getPlaceItemList()
        }
    }

    val currentWeatherLiveData: LiveData<NetworkResult<WeatherResponse>>
        get() = weatherRepository.weatherCurrentLiveData
    fun getCurrentWeather(lat: Float, lng: Float) {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather(lat.toString(), lng.toString())
        }
    }

    val currentKpIndexLiveData: LiveData<NetworkResult<KpResponse>>
        get() = auroraRepository.kpCurrentLiveData
    fun getCurrentKpIndex(dateString: String, hour: Int) {
        viewModelScope.launch {
            Log.d(TAG, "getCurrentKpIndex: vm go")
            auroraRepository.getCurrentKpIndex(dateString, hour)
        }
    }

} // End of AuroraViewModel