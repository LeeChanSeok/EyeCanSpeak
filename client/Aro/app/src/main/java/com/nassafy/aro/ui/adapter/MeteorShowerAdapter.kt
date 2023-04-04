package com.nassafy.aro.ui.adapter

import android.transition.*
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeteorShowerAdapter(var recyclerView: RecyclerView, var itemList: MutableList<MeteorShower>) :
    RecyclerView.Adapter<MeteorShowerAdapter.ViewHolder>() {
    private var expandedPosition = -1
    private var prevExpandedPosition = -1

    private fun createTransition(): Transition {
        val transitionSet = TransitionSet()
        val fade = Fade(Fade.MODE_OUT)
        fade.duration = 300

        val changeBounds = ChangeBounds()
        changeBounds.duration = 300

        transitionSet.addTransition(fade)
        transitionSet.addTransition(changeBounds)

        return transitionSet
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.meteor_shower_name_textview)
        val engNameTextView: TextView = itemView.findViewById(R.id.meteor_shower_eng_textview)
        val dateTextView: TextView = itemView.findViewById(R.id.meteor_shower_date_textview)
        val iconImageView: ImageView = itemView.findViewById(R.id.meteor_shower_imageview)
        val subImageView: ImageView = itemView.findViewById(R.id.meteor_shower_sub_imageview)
        val subItemView: ConstraintLayout = itemView.findViewById(R.id.item_sub_layout)
        val dateTextTextview: TextView = itemView.findViewById(R.id.meteor_shower_datetext_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_meteor_shower_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isExpanded = position == expandedPosition
        val item = itemList[position]

//        val iconPicasso = Picasso.get()
//            .load(item.image)
//            .fit()
//            .centerCrop()
//        iconPicasso.into(holder.iconImageView)

        if (isExpanded) {
            CoroutineScope(Dispatchers.Main).launch {
                val subImagePicasso = Picasso.get()
                    .load(item.subImage)
                    .fit().centerCrop()
                subImagePicasso.into(holder.subImageView)
            }
            prevExpandedPosition = holder.adapterPosition
        }

        holder.nameTextView.text = item.name
        holder.engNameTextView.text = item.engName
        holder.dateTextView.text = item.date
        holder.dateTextTextview.text = item.name



        holder.subItemView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.itemView.isActivated = isExpanded
        
        holder.itemView.setOnClickListener {
            val adapterPos = holder.adapterPosition
            if (adapterPos != RecyclerView.NO_POSITION) {
                expandedPosition = if (isExpanded) {
                    -1
                } else {
                    adapterPos
                }

                val transition = createTransition()
                TransitionManager.beginDelayedTransition(holder.subItemView.parent as ViewGroup, transition)

                notifyItemChanged(adapterPos)
                notifyItemChanged(prevExpandedPosition)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

}