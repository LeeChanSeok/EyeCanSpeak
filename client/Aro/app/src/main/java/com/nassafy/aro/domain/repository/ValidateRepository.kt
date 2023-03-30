package com.nassafy.aro.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nassafy.aro.domain.api.ValidateApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.di.WithoutHeaderInterceptorApi
import okhttp3.MultipartBody
import javax.inject.Inject

private const val TAG = "ValidateRepository_싸피"

class ValidateRepository @Inject constructor(
    @WithoutHeaderInterceptorApi private val validateApi: ValidateApi
) {

    // ====================================== Image Validate ===================================
    private val _postImageValidateResponseLiveData = MutableLiveData<NetworkResult<Int>>()
    val postImageValidateResponseLiveData: LiveData<NetworkResult<Int>>
        get() = _postImageValidateResponseLiveData

    suspend fun postImageValidate(validateImage: MultipartBody.Part?) {
        val response = validateApi.postImageValidate(validateImage)

        _postImageValidateResponseLiveData.postValue(NetworkResult.Loading())

        if (response.isSuccessful) {
            _postImageValidateResponseLiveData.postValue(
                NetworkResult.Success(response.code())
            )
        } else if (response.errorBody() != null) {
            _postImageValidateResponseLiveData.postValue(
                NetworkResult.Error(
                    response.errorBody()!!.string()
                )
            )
        }

    } // End of postImageValidate
} // End of ValidateRepository class
