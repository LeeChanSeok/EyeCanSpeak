package com.nassafy.aro.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.nassafy.aro.R
import com.nassafy.aro.databinding.ItemCountrySpinnerBinding

class CountrySpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val list: List<String>
) : ArrayAdapter<String>(context, resId, list) {

    override fun getCount(): Int = list.size
    override fun getItem(position: Int): String = list[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemCountrySpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val countryName = list[position]
        try {
            binding.itemCountrySpinnerTextview.text = countryName
            binding.itemCountrySpinnerTextview.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            binding.itemCountrySpinnerDivider.isGone = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemCountrySpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val countryName = list[position]
        try {
            binding.itemCountrySpinnerLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.view_pager_background_color
                )
            )
            binding.itemCountrySpinnerTextview.text = countryName
            binding.itemCountrySpinnerTextview.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            if (position == list.size - 1)
                binding.itemCountrySpinnerDivider.isGone = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

}