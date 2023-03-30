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

    // ============================================== 이미지 오로라 체크 ==============================================
    val postImageValidateResponseLiveData: LiveData<NetworkResult<Int>>
        get() = validateRepository.postImageValidateResponseLiveData

    suspend fun imageValidate(validateImageFile: MultipartBody.Part?) {
        viewModelScope.launch {
            validateRepository.postImageValidate(validateImageFile)
        }
    } // End of imageValidate
} // End of ValidateViewModel
