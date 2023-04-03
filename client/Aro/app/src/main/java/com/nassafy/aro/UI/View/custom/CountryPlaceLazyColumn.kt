package com.nassafy.aro.ui.view.custom

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nassafy.aro.R
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