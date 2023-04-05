package com.nassafy.aro.ui.view.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nassafy.aro.R

@Composable
fun ServiceNotSelectedDisplayLayout(displayedString: String) {
    Column(
        Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.fillMaxHeight(0.2f))
        Box() {
            Text(
                text = displayedString,
                style = TextStyle(
                    fontFamily = NanumSqaureFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                color = colorResource(id = R.color.main_app_color)
            ) // End of Text
        }
        Box(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = stringResource(id = R.string.my_page_not_select_service_inform_textview_text),
                style = TextStyle(
                    fontFamily = NanumSqaureFont,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                ),
                color = colorResource(id = R.color.main_app_color)
            ) // End of Text
        }

    }
}