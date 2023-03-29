package com.nassafy.aro.ui.view.dialog

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nassafy.aro.databinding.DialogOkBinding

class OkDialog(private val title: String, private val content: String, private val okButtonContent: String): DialogFragment() {

    private var _binding: DialogOkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogOkBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    } // End of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogTitleTextview.text = title
        binding.dialogContentTextview.text = content
        binding.dialogOkTextview.apply {
            text = okButtonContent
            paintFlags = Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                dismiss()
            }
        }
    } // End of onViewCreated

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    } // End of onDestroyView
}