package com.nassafy.aro.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.nassafy.aro.R

@SuppressLint("ViewConstructor")
class ChartMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private var chartMarkerTextview: TextView = findViewById(R.id.chart_marker_textview)

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        if (e is CandleEntry) {
            chartMarkerTextview!!.text = Utils.formatNumber(e.high, 0, true)
        } else {
            chartMarkerTextview!!.text = "${e.y}"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}