package com.nassafy.aro.data.dto

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place constructor(
    var id: Int, //아이디
    var nation: String, // 국가 이름
    val placeName: String, // 명소 이름
    val latitude: Float, // 위도
    val longitude: Float, // 경도
    val mapImage: String // 사진
) : ClusterItem, Parcelable {
    override fun getPosition(): LatLng {
        return LatLng(latitude.toDouble(), longitude.toDouble())
    }

    override fun getTitle(): String {
        return placeName
    }

    override fun getSnippet(): String {
        // TODO: 확률이 들어가야하지 않나???
        return placeName
    }

    override fun getZIndex(): Float {
        return 0.0F
    }

}
