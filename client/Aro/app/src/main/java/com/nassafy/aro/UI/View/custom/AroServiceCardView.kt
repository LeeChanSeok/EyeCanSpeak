package com.nassafy.aro.ui.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.nassafy.aro.R
import com.nassafy.aro.databinding.AroServiceCardviewLayoutBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import jp.wasabeef.picasso.transformations.GrayscaleTransformation


class AroServiceCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    val binding by lazy {
        AroServiceCardviewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    interface OnSelectedChangeListener {
        fun onSelectedChanged(isSelected: Boolean)
    }

    interface OnClickListener {
        fun onClick()
    }

    private var onSelectedChangeListener: OnSelectedChangeListener? = null
    var onClickListener: OnClickListener? = null

    init {
        val view = binding.root

        val styledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.AroServiceCardView, defStyleAttr, 0)
        val resourceId =
            styledAttributes.getResourceId(R.styleable.AroServiceCardView_service_image, 0)

        binding.serviceTextview.text =
            styledAttributes.getText(R.styleable.AroServiceCardView_service_name_text)
        styledAttributes.recycle()

        onSelectedChangeListener = object : OnSelectedChangeListener {
            override fun onSelectedChanged(isSelected: Boolean) {

                val grayScalePicasso = Picasso.get()
                    .load(resourceId)
                    .transform(
                        listOf(
                            BlurTransformation(context, 25, 1),
                            GrayscaleTransformation()
                        )
                    ).fit().centerCrop()
                val normalPicasso = Picasso.get()
                    .load(resourceId)
                    .fit().centerCrop()
                when (isSelected) {
                    true -> {
                        normalPicasso.into(binding.serviceImageview)
                    }
                    false -> {
                        grayScalePicasso.into(binding.serviceImageview)
                    }
                }
            }
        } //

        view.setOnClickListener {
            view.isSelected = !view.isSelected
            onSelectedChangeListener?.onSelectedChanged(view.isSelected)
            onClickListener?.onClick()
        }

    } // End of Init

    fun getIsSelected() = binding.root.isSelected

    fun setIsSelected(selected: Boolean) {
        binding.root.isSelected = selected
        onSelectedChangeListener?.onSelectedChanged(selected)
    }

    fun setOnSelectedChangeListener(listener: OnSelectedChangeListener) {
        onSelectedChangeListener = listener
    }

} // End of AroServiceCarView