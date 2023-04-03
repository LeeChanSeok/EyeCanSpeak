package com.nassafy.aro.ui.view

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.Place
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.util.generateBitmapDescriptorFromRes

class CustomMarkerRenderer(
    context: Context, googleMap: GoogleMap, clusterManager: ClusterManager<PlaceItem>) :
    DefaultClusterRenderer<PlaceItem>(context, googleMap, clusterManager) {

    private val customMarker = generateBitmapDescriptorFromRes(context, R.drawable.map_marker)

    override fun onBeforeClusterItemRendered(item: PlaceItem, markerOptions: MarkerOptions) {
        markerOptions
            .position(item.position)
            .title(item.title)
            .icon(customMarker)

        super.onBeforeClusterItemRendered(item, markerOptions)
    }

    override fun onClusterItemRendered(item: PlaceItem, marker: Marker) {
        super.onClusterItemRendered(item, marker)
        marker.tag = item
    }

}