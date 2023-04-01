package com.nassafy.aro.ui.view.custom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.ui.view.ServiceViewModel

@Composable
fun CountryPlaceChips(selectedPlaceList: MutableList<PlaceItem>, viewModel: ServiceViewModel) {
    LazyRow(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(1f)
            .padding(horizontal = 30.dp)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //TODO change items
        items(selectedPlaceList) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .height(40.dp)
            ) {
                ChipHasCancelButton(it, viewModel)
            } // End of RowScope
        } // End of items
    } // End of LazyRows
} // End of CountryPlaceChips

//@Preview(showBackground = true)
//@Composable
//fun DefaultCountryPlaceChipsPreview() {
//    CountryPlaceChips()
//}