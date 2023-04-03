package com.nassafy.aro.ui.view.custom

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentAroCountryPlaceSelectBinding
import com.nassafy.aro.ui.adapter.CountrySpinnerAdapter
import com.nassafy.aro.ui.view.BaseFragment

open class AroCountryPlaceSelectFragment : BaseFragment<FragmentAroCountryPlaceSelectBinding>(FragmentAroCountryPlaceSelectBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    } // End of onViewCreated


}
//
//@Preview(showBackground = true)
//@Composable
//fun preview() {
//    Column(
//        modifier = Modifier
//            .height(200.dp)
//    ) {
//        CountryPlaceChips()
//        Divider(
//            modifier = Modifier.height(2.dp),
//            color = Color.White
//        )
//        CountryPlaceLazyColumn()
//    }
//}