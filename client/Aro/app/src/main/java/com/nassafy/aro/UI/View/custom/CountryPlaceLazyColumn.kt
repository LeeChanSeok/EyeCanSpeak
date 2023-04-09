package com.nassafy.aro.ui.view.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.ui.view.ServiceViewModel

@Composable
fun CountryPlaceLazyColumn(
    placeList: MutableList<PlaceItem>,
    selectedPlaceList: MutableList<PlaceItem>,
    viewModel: ServiceViewModel
) {

    LazyColumn(modifier = Modifier.fillMaxWidth(0.9f)) {
        items(placeList) {
            CountryPlaceLazyColumnItem(it, selectedPlaceList, viewModel)
        }
    }
}