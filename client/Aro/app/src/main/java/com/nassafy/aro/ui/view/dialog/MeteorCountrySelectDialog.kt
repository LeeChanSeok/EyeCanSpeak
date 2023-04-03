package com.nassafy.aro.ui.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.nassafy.aro.databinding.DialogMeteorCountrySelectBinding
import com.nassafy.aro.ui.adapter.DateHourSelectAdapter
import com.nassafy.aro.ui.view.aurora.AuroraFragment
import com.nassafy.aro.ui.view.meteorshower.MeteorShowerFragment
import java.util.zip.Inflater

class MeteorCountrySelectDialog(
    var countryList: ArrayList<String>
) : DialogFragment() {
    private var _binding: DialogMeteorCountrySelectBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryAdapter: DateHourSelectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMeteorCountrySelectBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    } // End of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryAdapter = DateHourSelectAdapter(countryList)

//        binding.countryRecyclerview.apply {
//            adapter = countryAdapter
//            setAlpha(true)
//            setIntervalRatio(0.8f)
//            setOrientation(RecyclerView.VERTICAL)
//        }

        // TODO : 이제는 필요 없는듯
//        binding.okButton.setOnClickListener {
//            var countryIdx = binding.countryRecyclerview.getSelectedPosition()
//            val meteorShowerFragment = parentFragment as MeteorShowerFragment
////            meteorShowerFragment.changeCountry(countryList[countryIdx])
//            dismiss()
//        }
    }

    override fun onResume() {
        super.onResume()
        val width = ((context?.resources?.displayMetrics?.widthPixels ?: 0) * 0.9).toInt()
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    } // End of onResume
}