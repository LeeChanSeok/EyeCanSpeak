package com.nassafy.aro.ui.view.dialog

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.nassafy.aro.databinding.DialogOkCancelBinding

class OkCancelDialog(
    private val title: String,
    private val content: String,
    setOnOkButtonClickListener: SetOnOkButtonClickListener
) :
    DialogFragment() {

    private var _binding: DialogOkCancelBinding? = null
    private val binding get() = _binding!!

    interface SetOnOkButtonClickListener {
        fun onOkButtonClick()
    }

    private var setOnOkButtonClickListener: SetOnOkButtonClickListener? = null

    init {
        this.setOnOkButtonClickListener = setOnOkButtonClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogOkCancelBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    } // End of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogTitleTextview.text = title
        binding.dialogContentTextview.text = content
        binding.dialogOkTextview.apply {
            paintFlags = Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                setOnOkButtonClickListener?.onOkButtonClick()
                dismiss()
            }
        }
        binding.dialogCancelTextview.apply {
            paintFlags = Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                dismiss()
            }
        }
    } // End of onViewCreated

    override fun onResume() {
        super.onResume()
        val width = ((context?.resources?.displayMetrics?.widthPixels ?: 0) * 0.9).toInt()
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    } // End of onDestroyView
}