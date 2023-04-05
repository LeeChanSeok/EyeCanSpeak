package com.nassafy.aro.ui.view.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import coil.transform.GrayscaleTransformation
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.ui.view.ServiceViewModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CountryPlaceLazyColumnItem(
    place: PlaceItem,
    selectedPlaceList: MutableList<PlaceItem>,
    viewModel: ServiceViewModel
) {
    //TODO change isSelected to DTO's boolean type var
    var isSelected by remember { mutableStateOf(false) }
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .componentRegistry {
            add(SvgDecoder(LocalContext.current))
        }
        .build()

    DisposableEffect(key1 = place, key2 = selectedPlaceList.size) {
        isSelected = selectedPlaceList.any { it.placeName == place.placeName }
        onDispose {
            isSelected = selectedPlaceList.any { it.placeId == place.placeId }
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
                        viewModel.selectAuroraPlace(place)
                    }
                    false -> {
                        viewModel.unSelectAuroraPlace(place)
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
                CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                    val painter = rememberImagePainter(place.stamp, builder = {
                        when (isSelected) {
                            true -> {}
                            false -> {
                                transformations(
                                    GrayscaleTransformation(),       // Gray Scale Transformation
                                )
                            }
                        }
                    })
                    Image(
                        painter = painter,
                        contentDescription = "SVG Image",
                        modifier = Modifier
                            .weight(2f),
                        contentScale = ContentScale.FillWidth,
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = place.placeName,
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
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = place.description,
                            style = TextStyle(
                                fontFamily = NanumSqaureFont,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            ),
                            color = when (isSelected) {
                                false -> colorResource(id = R.color.dark_gray)
                                true -> Color.White
                            }
                        )
                    } // End of Box
                } // End of Column
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
} // End of


//@Preview(showBackground = true)
//@Composable
//fun DefaultCountryPlaceLazyColumnItemPreview() {
//    CountryPlaceLazyColumnItem(order = 1)
//}