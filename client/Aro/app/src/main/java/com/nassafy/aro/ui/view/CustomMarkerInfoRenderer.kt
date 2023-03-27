package com.nassafy.aro.ui.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.Place
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.weather.WeatherResponse
import com.nassafy.aro.domain.repository.WeatherRepository
import com.nassafy.aro.ui.view.aurora.AuroraViewModel
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.lang.Exception

private const val TAG = "InfoRenderer_SDR"

class CustomMarkerInfoRenderer(
    private val layoutInflater: LayoutInflater, val context: Context, val auroraViewModel: AuroraViewModel) :
    GoogleMap.InfoWindowAdapter {

    private var infoWindow = layoutInflater.inflate(R.layout.map_info_window, null)
    private var lastMarker : Marker? = null

    override fun getInfoContents(marker: Marker): View? {
        return null
    } // End of getInfoContents

    override fun getInfoWindow(marker: Marker): View {
        if (lastMarker != marker) {
            Log.d(TAG, "getInfoWindow: ${marker.title}")
            Log.d(TAG, "position: ${marker.position}")
            
            lastMarker = marker
            val placeItem: PlaceItem = marker.tag as PlaceItem

            Log.d(TAG, "getInfoWindow: $placeItem")

            val infoImageView = infoWindow.findViewById<ImageView>(R.id.map_info_imageview)
            val infoNameTextView = infoWindow.findViewById<TextView>(R.id.map_info_name_textview)
            val infoTextView = infoWindow.findViewById<TextView>(R.id.map_info_textview)

            val infoLinearLayout = infoWindow.findViewById<LinearLayout>(R.id.map_info_linearlayout)
            val infoProgressBar = infoWindow.findViewById<ProgressBar>(R.id.map_info_progressbar)


            infoProgressBar.visibility = View.VISIBLE
            infoLinearLayout.visibility = View.INVISIBLE
            infoNameTextView.text = placeItem.placeName
//            infoImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.loading_spinner))

            CoroutineScope(Dispatchers.Main).launch {
//                val currentWeatherLiveData = auroraViewModel.currentWeatherLiveData

                val resultPicture: Deferred<Int> = async {
                    val picasso = Picasso.get()
                        .load(placeItem.mapImage)
                        .resize(infoImageView!!.width, infoImageView.height)
                        .centerCrop()
                        .noFade()
                        .into(infoImageView, object : Callback {
                            override fun onSuccess() {
                                when {
                                    marker.isInfoWindowShown -> {
                                                    infoTextView.text = "${marker.title}"
                                                    infoProgressBar.visibility = View.VISIBLE
                                                    infoLinearLayout.visibility = View.VISIBLE
                                                    marker.showInfoWindow()
//                                        currentWeatherLiveData.observeForever { result ->
//                                            when (result) {
//                                                is NetworkResult.Success -> {
//                                                    val weatherResponse = result.data
//                                                    infoTextView.text = weatherResponse?.weather?.get(0)?.main ?: "NULL"
//                                                    infoProgressBar.visibility = View.INVISIBLE
//                                                    infoLinearLayout.visibility = View.VISIBLE
////                                                    infoImageView.visibility = View.VISIBLE
////                                                    infoTextView.visibility = View.VISIBLE
//                                                    marker.showInfoWindow()
//                                                }
//                                                is NetworkResult.Error -> {
//                                                    Log.e(TAG, "Error")
//                                                }
//                                                is NetworkResult.Loading -> {}
//                                            }
//                                        }
                                    }
                                }
                            }
                            override fun onError(e: Exception?) {
                                Log.e(TAG, "onError: $e")
                            }
                        })
                    0
                }
                awaitAll(resultPicture)
            }
        }
        return infoWindow
    } // End of getInfoWindow

} // End of CustomMarkerInfoRenderer