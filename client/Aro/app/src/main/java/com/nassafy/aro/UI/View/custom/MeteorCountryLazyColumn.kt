package com.nassafy.aro.ui.view.custom

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nassafy.aro.data.dto.MeteorCountry
import com.nassafy.aro.ui.view.ServiceViewModel

@Composable
fun MeteorCountryLazyColumn(
    meteorCountryList: MutableList<MeteorCountry>, // Todo convert to Country
    selectedCountry: MeteorCountry?,
    viewModel: ServiceViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.9f)
    ) {
        items(meteorCountryList) { country ->
            MeteorCountryLazyColumItem(
                country,
                selectedCountry,
                viewModel
            )
        }
    }
}