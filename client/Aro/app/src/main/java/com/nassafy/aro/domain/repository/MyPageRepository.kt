package com.nassafy.aro.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.FavoriteList
import com.nassafy.aro.data.dto.MeteorCountry
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.domain.api.MyPageApi
import com.nassafy.aro.domain.api.WithoutHeaderMyPageApi
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.setNetworkResult
import javax.inject.Inject

private const val TAG = "ssafy_pcs"
class MyPageRepository @Inject constructor(private val myPageApi: MyPageApi, private val withoutHeaderMyPageApi: WithoutHeaderMyPageApi) {

    private val _nicknameLiveData = MutableLiveData<NetworkResult<Unit>>()
    val nicknameLiveData get() = _nicknameLiveData

    private val _getSelectedServiceNetworkResultLiveData = MutableLiveData<NetworkResult<UserTest>>()
    val getSelectedServiceNetworkResultLiveData get() = _getSelectedServiceNetworkResultLiveData

    private val _favoriteListNetworkResultLiveData = MutableLiveData<NetworkResult<FavoriteList>>()
    val favoriteListNetworkResultLiveData: LiveData<NetworkResult<FavoriteList>> get() = _favoriteListNetworkResultLiveData

    private val _setSelectServiceNetworkResultLiveData = MutableLiveData<NetworkResult<JsonObject>>()
    val setSelectServiceNetworkResultLiveData get() = _setSelectServiceNetworkResultLiveData

    private val _countryListNetworkResultLiveData = MutableLiveData<NetworkResult<List<String>>>()
    val countryListNetworkResultLiveData: LiveData<NetworkResult<List<String>>> get() = _countryListNetworkResultLiveData

    private val _placeListNetworkResultLiveData = MutableLiveData<NetworkResult<List<PlaceItem>>>()
    val placeListNetworkResultLiveData: LiveData<NetworkResult<List<PlaceItem>>> get() = _placeListNetworkResultLiveData

    private val _postFavoriteListNetworkResultLiveData = MutableLiveData<NetworkResult<Unit>>()
    val postFavoriteListNetworkResultLiveData: LiveData<NetworkResult<Unit>> get() = _postFavoriteListNetworkResultLiveData

    private val _deleteFavoriteNetworkResultLiveData = MutableLiveData<NetworkResult<Long>>()
    val deleteFavoriteNetworkResultLiveData: LiveData<NetworkResult<Long>> get() = _deleteFavoriteNetworkResultLiveData

    private val _meteorCountryListNetworkResultLiveData = MutableLiveData<NetworkResult<List<MeteorCountry>>>()
    val meteorCountryListNetworkResultLiveData: LiveData<NetworkResult<List<MeteorCountry>>> get() = _meteorCountryListNetworkResultLiveData


    suspend fun changeNickname(nickname: String) {
        val response = myPageApi.changeNickname(nickname = nickname)
        _nicknameLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _nicknameLiveData.postValue(
                        NetworkResult.Success(
                            response.body()!!
                        )
                    )
                }
                response.errorBody() != null -> {
                    _nicknameLiveData.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                }
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    } // End of changeNickname

    suspend fun getFavoriteList() {
        val response = myPageApi.getFavoriteList()
        _favoriteListNetworkResultLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _favoriteListNetworkResultLiveData.postValue(NetworkResult.Success(
                        response.body()!!
                    ))

                }
                response.errorBody() != null -> {
                    _favoriteListNetworkResultLiveData.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                }
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    } // End of changeNickname

    suspend fun getSelectedServiceList() {
        val response = myPageApi.getSelectedService()
        _getSelectedServiceNetworkResultLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _getSelectedServiceNetworkResultLiveData.postValue(
                        NetworkResult.Success(
                            response.body()!!
                        )
                    )
                }
                response.errorBody() != null -> {
                    _getSelectedServiceNetworkResultLiveData.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                }
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    } // End of getSelectedServiceList

    suspend fun selectService(auroraService: Boolean, meteorService: Boolean) {
        val response = myPageApi.selectService(
            JsonObject().apply {
                addProperty("auroraService", auroraService)
                addProperty("meteorService", meteorService)
            }
        )
        _setSelectServiceNetworkResultLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _setSelectServiceNetworkResultLiveData.postValue(
                        NetworkResult.Success(JsonObject())
                    )
                }
                response.errorBody() != null -> {
                    _setSelectServiceNetworkResultLiveData.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                }
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    } // End of selectService

    suspend fun getCountryList() {
        val response = withoutHeaderMyPageApi.getCountryList()
        _countryListNetworkResultLiveData.setNetworkResult(response)
    } // End of getCountryList

    suspend fun getPlaceList(nation: String) {
        val response = withoutHeaderMyPageApi.getPlaceList(nation)
        _placeListNetworkResultLiveData.setNetworkResult(response)
    } // End of getPlaceList

    suspend fun getMeteorCountryList() {
        val response = myPageApi.getMeteorCountryList()
        Log.d(TAG, "getMeteorCountryList: ${response.body()}")
        _meteorCountryListNetworkResultLiveData.setNetworkResult(response)
    }

    suspend fun postFavoriteList(requestBody: JsonObject) {
        val response = myPageApi.postFavoriteList(requestBody)
        _postFavoriteListNetworkResultLiveData.setNetworkResult(response)
    } // End of postFavoriteList

    suspend fun deleteFavorite(interestId: Long) {
        val response = myPageApi.deleteFavorite(interestId)
        _deleteFavoriteNetworkResultLiveData.postValue(NetworkResult.Loading())
        try {
            when {
                response.isSuccessful -> {
                    _deleteFavoriteNetworkResultLiveData.postValue(NetworkResult.Success(
                        interestId
                    ))

                }
                response.errorBody() != null -> {
                    _deleteFavoriteNetworkResultLiveData.postValue(NetworkResult.Error(response.errorBody()!!.string()))
                }
            } // End of when
        } catch (e: java.lang.Exception) {
            Log.e("ssafy", "getServerCallTest: ${e.message}")
        } // End of try-catch
    }

} // End of MyPageRepository