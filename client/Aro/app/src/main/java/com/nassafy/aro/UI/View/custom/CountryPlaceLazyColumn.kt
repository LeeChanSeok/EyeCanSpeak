package com.nassafy.aro.ui.view.custom

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.ui.view.ServiceViewModel

@Composable
fun CountryPlaceLazyColumn(placeList: MutableList<PlaceItem>, selectedPlaceList: MutableList<PlaceItem>, viewModel: ServiceViewModel ) {
    LazyColumn (
        Modifier.fillMaxWidth(0.9f)
            ){
        // TODO Change items DTO List
        items(placeList) {
            CountryPlaceLazyColumnItem(it, selectedPlaceList, viewModel)
        }
    }
}
