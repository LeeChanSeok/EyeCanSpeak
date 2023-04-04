package com.nassafy.aro.ui.view.meteorshower

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.data.dto.kp.MyMeteor
import com.nassafy.aro.domain.repository.MeteorShowerRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeteorShowerViewModel @Inject constructor(
    private val meteorShowerRepository: MeteorShowerRepository
) : ViewModel() {

    // ============================================= 관심 국가 유성우 데이터 가져오기 =============================================
    val getMyMeteorShowerResponseLiveData: LiveData<NetworkResult<MyMeteor>>
        get() = meteorShowerRepository.getMyMeteorShowerResponseLiveData

    suspend fun getMyMeteorShower() {
        viewModelScope.launch {
            meteorShowerRepository.getMyMeteorShower()
        }
    } // End of getMyMeteorShower

} // End of MeteorShowerViewModel class
