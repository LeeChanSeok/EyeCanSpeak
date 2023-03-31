package com.nassafy.aro.ui.adapter

import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.MeteorShower
import com.squareup.picasso.Picasso

class MeteorShowerAdapter(var itemList: MutableList<MeteorShower>) :
    RecyclerView.Adapter<MeteorShowerAdapter.ViewHolder>() {
    private var expandedPosition = -1
    private var prevExpandedPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.fragment_meteor_shower_recyclerview_item,
                parent, false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isExpanded = position == expandedPosition

        var item = itemList[position]

        var nameTextView = holder.itemView.findViewById<TextView>(R.id.meteor_shower_name_textview)
        var engNameTextView =
            holder.itemView.findViewById<TextView>(R.id.meteor_shower_eng_textview)
        var dateTextView = holder.itemView.findViewById<TextView>(R.id.meteor_shower_date_textview)
        var iconImageView = holder.itemView.findViewById<ImageView>(R.id.meteor_shower_imageview)
        var subImageView = holder.itemView.findViewById<ImageView>(R.id.meteor_shower_sub_imageview)
        var subItemView = holder.itemView.findViewById<ConstraintLayout>(R.id.item_sub_layout)


        val iconPicasso = Picasso.get()
            .load(item.image)
            .fit().centerCrop()
        iconPicasso.into(iconImageView)
        val subImagePicasso = Picasso.get()
            .load(item.subImage)
            .fit().centerCrop()
        subImagePicasso.into(subImageView)

        nameTextView.text = item.name
        engNameTextView.text = item.engName
        dateTextView.text = item.date

        subItemView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.itemView.isActivated = isExpanded

        if(isExpanded) prevExpandedPosition = position

        holder.itemView.setOnClickListener {
            expandedPosition = if (isExpanded) {
                -1
            } else {
                position
            }
            notifyItemChanged(position)
            notifyItemChanged(prevExpandedPosition)
        }   // End of setOnClickListener

    } // End of onBindViewHolder

    override fun getItemCount(): Int = itemList.size
}