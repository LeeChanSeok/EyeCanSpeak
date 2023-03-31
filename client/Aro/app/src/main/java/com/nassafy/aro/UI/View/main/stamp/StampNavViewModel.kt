package com.nassafy.aro.ui.view.main.stamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nassafy.aro.data.dto.UserStampPlace

class StampNavViewModel : ViewModel() {

    // ======================================== stampFragment 국가 리스트 ========================================
    private val _countryList: MutableList<String> = ArrayList()
    val countryList: List<String>
        get() = _countryList

    fun setCountryList(countryList: List<String>) {
        _countryList.addAll(countryList)
    } // End of countryListSet

    // ======================================== stampCountryPlaceFragment 국가 명소데이터 및 유저 데이터 ========================================
    private val _userCountryPlaceDataList: MutableList<UserStampPlace> = ArrayList()
    val userCountryPlaceDataList: List<UserStampPlace>
        get() = _userCountryPlaceDataList

    fun setUserCountryPlaceDataList(dataList: List<UserStampPlace>) {
        _userCountryPlaceDataList.clear()
        _userCountryPlaceDataList.addAll(dataList)
    } // End of setUserCountryPlaceData

    // ======================================== 선택된 국가의 명소 데이터 ========================================
    private val _selectedPlaceLiveData = MutableLiveData<UserStampPlace>()
    val selectedPlaceLiveData: LiveData<UserStampPlace>
        get() = _selectedPlaceLiveData

    fun setSelectedPlaceLiveData(newSelectedPlace: UserStampPlace) {
        _selectedPlaceLiveData.value = newSelectedPlace
    } // End of setSelectedPlaceLiveData

    // ======================================== spinner를 통해서 현재 선택된 국가 ========================================
    private var _nowSelectedCountry: String = ""
    val nowSelectedCountry: String
        get() = _nowSelectedCountry

    fun setSelectedCountry(countryName: String) {
        _nowSelectedCountry = countryName
    } // End of setSelectedCountry

    // ======================================== 명소 뷰페이저에서 현재 선택된 명소의 명소ID ========================================
    private var _nowSelectedAttractionId: Long = 0L
    val nowSelectedAttractionId: Long
        get() = _nowSelectedAttractionId

    fun setNowSelectedAttractionId(attractionId: Long) {
        _nowSelectedAttractionId = attractionId
    } // End of setNowSelectedAttractionId
} // End of StampNavViewModel
