package com.nassafy.aro.ui.view.custom

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.ui.view.ServiceViewModel

@Composable
fun ChipHasCancelButton(place: PlaceItem, viewModel: ServiceViewModel) {
    Row(
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.height(28.dp), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = place.placeName,
                modifier = Modifier.padding(start = 16.dp),
                style = TextStyle(
                    fontFamily = NanumSqaureFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
        }
        Box (Modifier.height(28.dp), contentAlignment = Alignment.Center) {
            IconButton(onClick = {
                viewModel.unSelectAuroraPlace(place)
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.Black,
                    modifier = Modifier.padding(end = 0.dp).height(18.dp).width(18.dp)
                )
            }
        }
    }
}