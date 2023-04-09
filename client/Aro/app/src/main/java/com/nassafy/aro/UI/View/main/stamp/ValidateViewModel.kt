package com.nassafy.aro.ui.view.main.stamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassafy.aro.domain.repository.ValidateRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ValidateViewModel @Inject constructor(
    private val validateRepository: ValidateRepository
) : ViewModel() {

    // ======================================  이미지 오로라 여부 플라스크로 확인하기 ===================================
    val postImageValidateResponseLiveData: LiveData<NetworkResult<Int>>
        get() = validateRepository.postImageValidateResponseLiveData

    suspend fun imageValidate(validateImageFile: MultipartBody.Part?) {
        viewModelScope.launch {
            validateRepository.postImageValidate(validateImageFile)
        }
    } // End of imageValidate

    // ====================================== Image Validate Success ===================================
    val postImageValidateSuccessResponseLiveData: LiveData<NetworkResult<Int>>
        get() = validateRepository.postImageValidateSuccessResponseLiveData

    suspend fun postImageValidateSuccess(attractionId: Long) {
        viewModelScope.launch {
            validateRepository.postImageValidateSuccess(attractionId)
        }
    } // End of postImageValidateSuccess
} // End of ValidateViewModel
