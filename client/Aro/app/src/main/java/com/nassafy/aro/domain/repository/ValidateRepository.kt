package com.nassafy.aro.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nassafy.aro.domain.api.ValidateApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.di.HeaderInterceptorApi
import com.nassafy.aro.util.di.WithoutHeaderInterceptorApi
import okhttp3.MultipartBody
import javax.inject.Inject

private const val TAG = "ValidateRepository_싸피"

class ValidateRepository @Inject constructor(
    @WithoutHeaderInterceptorApi private val validateApi: ValidateApi,
    @HeaderInterceptorApi private val validateHeaderApi: ValidateApi

) {

    // ======================================  이미지 오로라 여부 플라스크로 확인하기 ===================================
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

    // ====================================== Image Validate Success ===================================
    private val _postImageValidateSuccessResponseLiveData = MutableLiveData<NetworkResult<Int>>()
    val postImageValidateSuccessResponseLiveData: LiveData<NetworkResult<Int>>
        get() = _postImageValidateSuccessResponseLiveData

    suspend fun postImageValidateSuccess(attractionId: Long) {
        val response = validateHeaderApi.postImageValidateSuccess(attractionId)

        _postImageValidateSuccessResponseLiveData.postValue(NetworkResult.Loading())

        if (response.isSuccessful) {
            _postImageValidateSuccessResponseLiveData.postValue(
                NetworkResult.Success(response.code())
            )
        } else if (response.errorBody() != null) {
            _postImageValidateSuccessResponseLiveData.postValue(
                NetworkResult.Error(
                    response.errorBody()!!.string()
                )
            )
        }
    } // End of postValidateSuccess
} // End of ValidateRepository class
