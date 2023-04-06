package com.nassafy.aro.ui.view

import androidx.lifecycle.ViewModel
import com.nassafy.aro.data.dto.MeteorCountry
import com.nassafy.aro.data.dto.PlaceItem

abstract class ServiceViewModel : ViewModel() {
    abstract var isAuroraServiceSelected: Boolean
    abstract var isMeteorServiceSelected: Boolean
    abstract fun selectAuroraPlace(placeItem: PlaceItem)
    abstract fun unSelectAuroraPlace(placeItem: PlaceItem)
    abstract fun selectMeteorCountry(country: MeteorCountry)
    abstract fun unSelectMeteorCountry()
}