package com.nassafy.aro.ui.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nassafy.aro.databinding.DialogOkBinding
import com.nassafy.aro.databinding.DialogOkCancelBinding

abstract class OkCancelDialog(private val title: String, private val content: String) :
    DialogFragment() {

    private var _binding: DialogOkCancelBinding? = null
    private val binding get() = _binding!!

    interface SetOnOkButtonClickListener {
        fun onOkButtonClick()
    }

    abstract val setOnOkButtonClickListener: SetOnOkButtonClickListener

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
        binding.dialogOkTextview.setOnClickListener {
            dismiss()
        }
        binding.dialogCancelTextview.setOnClickListener {
            setOnOkButtonClickListener.onOkButtonClick()
            dismiss()
        }

    } // End of onViewCreated

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    } // End of onDestroyView
}