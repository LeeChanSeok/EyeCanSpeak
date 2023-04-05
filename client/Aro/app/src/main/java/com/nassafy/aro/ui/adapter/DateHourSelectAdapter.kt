package com.nassafy.aro.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nassafy.aro.R

class DateHourSelectAdapter(var itemList: MutableList<String>) :
    RecyclerView.Adapter<DateHourSelectAdapter.DateHourSelectViewHolder>() {

    inner class DateHourSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView = itemView.findViewById(R.id.dialog_country_item_textview)

        fun bindInfo(item: String) {
            itemTitle.text = item
        }
    } // End of DateHourSelectViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHourSelectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_country_item, parent, false)

        return DateHourSelectViewHolder(view)
    } // End of onCreateViewHolder

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: DateHourSelectViewHolder, position: Int) {
        holder.bindInfo(itemList[position])
    } // End of onBindViewHolder

} // End of DateHourSelectAdapter