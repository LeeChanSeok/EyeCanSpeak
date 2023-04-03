package com.nassafy.aro.ui.view.main.stamp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.UserStampPlace
import com.nassafy.aro.databinding.StampCountryPlaceListItemBinding
import com.squareup.picasso.Picasso

private const val TAG = "CountryPlaceViewPagerAd_싸피"

class CountryPlaceViewPagerAdapter(
    private val countryName: String,
    private val countryPlaceList: List<UserStampPlace>
) : RecyclerView.Adapter<CountryPlaceViewPagerAdapter.CountryPlaceHolder>() {
    private lateinit var binding: StampCountryPlaceListItemBinding

    inner class CountryPlaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindInfo(data: UserStampPlace) {
//            binding.stampCountryNameTextview.text = data.countryName.toString()
//            binding.stampCountryPlaceNameTextview.text = data.placeName.toString()
//            binding.stampCountryPlaceImageview.setImageResource(data.image)
//            binding.stampCountryPlaceInformTextview.text = data.countryDetail.toString()
        } // End of bindInfo
    } // End of CountryPlaceHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryPlaceHolder {
        binding = StampCountryPlaceListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryPlaceHolder(binding.root)
    } // End of onCreateViewHolder

    override fun onBindViewHolder(holder: CountryPlaceHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${countryPlaceList}")

        holder.itemView.findViewById<TextView>(R.id.stamp_country_name_textview).text = countryName
        holder.itemView.findViewById<TextView>(R.id.stamp_country_place_name_textview).text =
            countryPlaceList[position].attractionName.toString()

        Picasso.get().load(countryPlaceList[position].auth!!.toUri()).fit().centerCrop()
            .into(holder.itemView.findViewById<ImageView>(R.id.stamp_country_place_imageview))

        holder.itemView.findViewById<TextView>(R.id.stamp_country_place_inform_textview).text =
            countryPlaceList[position].description.toString()


        /*
            인증하기 버튼일 때는 인증하는 화면으로 이동해야됨.
         */
        when (countryPlaceList[position].certification) {
            false -> {
                holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_write_diary_button).visibility =
                    View.GONE
                holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_validate_button).visibility =
                    View.VISIBLE

                holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_write_diary_button).isClickable =
                    false

                holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_validate_button).isClickable =
                    true
            }
            true -> {
                holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_write_diary_button).visibility =
                    View.VISIBLE
                holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_validate_button).visibility =
                    View.GONE

                holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_write_diary_button).isClickable =
                    true

                holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_validate_button).isClickable =
                    false
            }
            else -> {
                // Nothing
            }
        }

        holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_write_diary_button)
            .setOnClickListener {
                itemClickListener.writeDiaryButtonClick(position)
            }

        holder.itemView.findViewById<AppCompatButton>(R.id.stamp_country_place_validate_button)
            .setOnClickListener {
                itemClickListener.validateButtonclick(position)
            }

    } // End of onBindViewHolder

    override fun getItemCount(): Int = countryPlaceList.size

    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        // 일기 작성 클릭
        fun writeDiaryButtonClick(position: Int)

        // 인증 클릭
        fun validateButtonclick(position: Int)
    } // End of ItemClickListener interface

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    } // End of setItemClickListener
} // End of CountryPlaceViewPagerAdapter class
