package com.nassafy.aro.ui.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.ui.view.aurora.AuroraViewModel
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
            lastMarker = marker
            val placeItem: PlaceItem = marker.tag as PlaceItem
            val infoImageView = infoWindow.findViewById<ImageView>(R.id.map_info_imageview)
            val infoNameTextView = infoWindow.findViewById<TextView>(R.id.map_info_name_textview)
            val infoTextView = infoWindow.findViewById<TextView>(R.id.map_info_textview)

            val infoLinearLayout = infoWindow.findViewById<LinearLayout>(R.id.map_info_linearlayout)
            val infoProgressBar = infoWindow.findViewById<ProgressBar>(R.id.map_info_progressbar)

            infoLinearLayout.visibility = View.INVISIBLE
            infoNameTextView.text = placeItem.placeName

            CoroutineScope(Dispatchers.Main).launch {
                val resultPicture: Deferred<Int> = async {
                    val picasso = Picasso.get()
                        .load(placeItem.image)
                        .resize(infoImageView!!.width, infoImageView.height)
                        .centerCrop()
                        .noFade()
                        .into(infoImageView, object : Callback {
                            override fun onSuccess() {
                                when {
                                    marker.isInfoWindowShown -> {
                                                    infoTextView.text = "${placeItem.prob}%"
                                                     infoLinearLayout.visibility = View.VISIBLE
                                                    marker.showInfoWindow()
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