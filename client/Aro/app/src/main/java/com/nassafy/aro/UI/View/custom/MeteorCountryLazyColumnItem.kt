package com.nassafy.aro.ui.view.custom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.MeteorCountry
import com.nassafy.aro.ui.view.ServiceViewModel

private const val TAG = "ssafy_pcs"

@Composable
fun MeteorCountryLazyColumItem(
    meteorCountry: MeteorCountry,
    selectedCountry: MeteorCountry?,
    viewModel: ServiceViewModel
) {
    var isSelected = when (selectedCountry) {
        null -> {
            false
        }
        else -> {
            meteorCountry == selectedCountry
        }
    }
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                isSelected = !isSelected
                when (isSelected) {
                    true -> {
                        viewModel.selectMeteorCountry(meteorCountry)
                    }
                    false -> {
                        viewModel.unSelectMeteorCountry()
                    }
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(0.95f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(2f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = meteorCountry.countryEmoji,
                        style = TextStyle(
                            fontFamily = NanumSqaureFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(7f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = meteorCountry.countryName,
                        style = TextStyle(
                            fontFamily = NanumSqaureFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = when (isSelected) {
                            false -> colorResource(id = R.color.dark_gray)
                            true -> Color.White
                        } // end of when
                    ) // End of Text
                } // End of Box
                Icon(
                    imageVector = Icons.Outlined.Done,
                    contentDescription = "checked",
                    tint = colorResource(id = R.color.light_dark_gray),
                    modifier = Modifier
                        .alpha(if (isSelected) 1f else 0f)
                ) // End of Icon
            } // End of Row
            Spacer(modifier = Modifier.height(4.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(2.dp),
                color = colorResource(id = R.color.main_app_color),
            )
        } // End of Column
    } // End of Card

}