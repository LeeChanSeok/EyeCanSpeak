package com.nassafy.aro.util

import android.graphics.Color
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap


fun getKpPolylineOptions(kpIndex: Double): PolylineOptions {
    val polylineOptions = PolylineOptions()
    when {
        kpIndex >= 9 -> {
            polylineOptions.add(
                LatLng(38.0, -80.0), LatLng(41.0, -60.0),
                LatLng(46.0, -40.0), LatLng(50.0, -20.0),
                LatLng(52.0, 0.0), LatLng(52.1, 20.0),
                LatLng(52.2, 40.0), LatLng(52.5, 60.0),
                LatLng(52.8, 80.0), LatLng(53.0, 100.0),
                LatLng(53.0, 120.0), LatLng(54.5, 140.0),
                LatLng(54.2, 160.0), LatLng(53.2, 180.0),
                LatLng(50.2, -160.0), LatLng(48.0, -140.0),
                LatLng(42.5, -120.0), LatLng(39.0, -100.0),
                LatLng(38.0, -80.0)
            )
                .color(Color.HSVToColor(150, floatArrayOf(8F, 0.81F, 1.0F)))
            // Red
        } // End of kpIndex 9
        kpIndex >= 7 -> {
            polylineOptions.add(
                LatLng(42.0, -80.0), LatLng(45.0, -60.0),
                LatLng(50.0, -40.0), LatLng(53.0, -20.0),
                LatLng(55.0, 0.0), LatLng(56.0, 20.0),
                LatLng(56.2, 40.0), LatLng(56.5, 60.0),
                LatLng(56.8, 80.0), LatLng(57.0, 100.0),
                LatLng(57.5, 120.0), LatLng(58.5, 140.0),
                LatLng(58.5, 160.0), LatLng(57.5, 180.0),
                LatLng(55.0, -160.0), LatLng(50.5, -140.0),
                LatLng(46.5, -120.0), LatLng(42.5, -100.0),
                LatLng(42.0, -80.0),
            )
                .color(Color.HSVToColor(150, floatArrayOf(60F, 0.50F, 1.0F)))
            // Yellow
        } // End of kpIndex 7
        kpIndex >= 5 -> {
            polylineOptions.add(
                LatLng(46.0, -80.0), LatLng(49.1, -60.0),
                LatLng(53.0, -40.0), LatLng(57.0, -20.0),
                LatLng(59.0, 0.0), LatLng(59.9, 20.0),
                LatLng(60.2, 40.0), LatLng(60.5, 60.0),
                LatLng(60.8, 80.0), LatLng(61.0, 100.0),
                LatLng(61.5, 120.0), LatLng(62.5, 140.0),
                LatLng(62.3, 160.0), LatLng(61.3, 180.0),
                LatLng(58.5, -160.0), LatLng(54.5, -140.0),
                LatLng(50.2, -120.0), LatLng(47.0, -100.0),
                LatLng(46.0, -80.0),
            )
                .color(Color.HSVToColor(150, floatArrayOf(144F, 0.58564F, 0.7098F)))
            // Green
        } // End of kpIndex 5
        kpIndex == -1.0 -> {

        }
        else -> {
            polylineOptions.add(
                LatLng(50.1, -80.0), LatLng(53.0, -60.0),
                LatLng(57.0, -40.0), LatLng(60.0, -20.0),
                LatLng(62.5, 0.0), LatLng(63.7, 20.0),
                LatLng(64.5, 40.0), LatLng(64.5, 60.0),
                LatLng(64.8, 80.0), LatLng(65.0, 100.0),
                LatLng(65.4, 120.0), LatLng(66.4, 140.0),
                LatLng(66.3, 160.0), LatLng(65.0, 180.0),
                LatLng(62.8, -160.0), LatLng(59.0, -140.0),
                LatLng(54.6, -120.0), LatLng(51.5, -100.0),
                LatLng(50.1, -80.0)
            )
                .color(Color.HSVToColor(150, floatArrayOf(199F, 0.901F, 0.988F)))
            // Blue
        } // End of else
    }
    with(polylineOptions) {
        width(40F)
        startCap(RoundCap())
        endCap(RoundCap())
        jointType(JointType.ROUND)
        clickable(false)
        geodesic(true)
        zIndex(-1.0F)
    }
    return polylineOptions
} // End of getKpPolylineOptions

fun getKpPolylineTolerance(zoom: Float): Double {
    var tolerance: Double = 0.0
    tolerance = when {
        zoom <= 2 -> {
            250000.0
        }
        else -> {
            val divide = (zoom - 2.0F) * 5
            (250000.0F / divide).toDouble()
        }
    }
    return tolerance
} // End of getKpPolylineTolerance