package com.nassafy.aro.data.dto

import android.os.Parcelable
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceItem(
    @SerializedName("attractionName") val placeName: String,
    @SerializedName("attractionId") val placeId: Int,
    @SerializedName("description") val description: String,
    @SerializedName("stamp") val stamp: String,
    @SerializedName("mapImage") val mapImage : String,
    @SerializedName("image") val image: String,
    @SerializedName("latitude") val latitude : Float,
    @SerializedName("longitude") val longitude : Float,
    val interest: Boolean = false,
    val interestId: Long = 0,
    val prob: Int = 0
) : ClusterItem, Parcelable {
    override fun getPosition(): LatLng {
        return LatLng(latitude.toDouble(), longitude.toDouble())
    }

    override fun getTitle(): String {
        return placeName
    }

    override fun getSnippet(): String {
        return placeName
    }

    override fun getZIndex(): Float {
        return 0.0F
    }

    override fun hashCode(): Int {
        return placeId
    }
}