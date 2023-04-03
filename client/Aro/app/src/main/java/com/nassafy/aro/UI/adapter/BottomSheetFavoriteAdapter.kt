package com.nassafy.aro.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.kp.Probability

class BottomSheetFavoriteAdapter(var probs: List<Probability>) :
    RecyclerView.Adapter<BottomSheetFavoriteAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_recyclerview_item, parent, false)
        return ViewHolder(view)
    } // End of onCreateViewHolder

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var probability = probs[position]
        var mColor = R.color.over_70
        var mImage = R.drawable.over_triangle
        when {
            probability.prob >= 70 -> {

            }
            probability.prob >= 50 -> {
                mColor = R.color.over_50
            }
            probability.prob < 20 -> {
                mColor = R.color.under_20
                mImage = R.drawable.under_triangle
            }
            probability.prob < 50 -> {
                mColor = R.color.under_50
                mImage = R.drawable.under_triangle
            }
        }

        val weatherImage = when (probability.weather) {
            "Thunderstorm" -> R.drawable.weather_thunderstorm_icon
            "Drizzle" -> R.drawable.weather_drizzle_icon
            "Rain" -> R.drawable.weather_rain_icon
            "Snow" -> R.drawable.weather_snow_icon
            "Atmosphere" -> R.drawable.weather_atmosphere_icon
            "Clear" -> R.drawable.weather_clear_icon
            "Clouds" -> R.drawable.weather_clouds_icon
            else -> R.drawable.weather_clear_icon
        }
        var nameTextView = holder.itemView.findViewById<TextView>(R.id.bottom_sheet_item_name_textview)
        var percentTextView = holder.itemView.findViewById<TextView>(R.id.bottom_sheet_item_percent_textview)
        var updownImageView = holder.itemView.findViewById<ImageView>(R.id.bottom_sheet_item_updown_imageview)
        var weatherImageView = holder.itemView.findViewById<ImageView>(R.id.bottom_sheet_item_weather_imageview)

        nameTextView.text = probability.attractionName
        percentTextView.apply {
            text = "${probability.prob}%"
            setTextColor(ContextCompat.getColor(holder.itemView.context, mColor))
        }
        updownImageView.apply {
            setImageResource(mImage)
            setColorFilter(ContextCompat.getColor(holder.itemView.context, mColor))
        }
        weatherImageView.apply {
            setImageResource(weatherImage)
        }
    } // End of onBindViewHolder

    override fun getItemCount() = probs.size
} // End of BottomSheetFavoriteAdapter