package com.nassafy.aro.ui.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.nassafy.aro.databinding.DialogDateHourSelectBinding
import com.nassafy.aro.ui.adapter.DateHourSelectAdapter
import com.nassafy.aro.ui.view.aurora.AuroraFragment

class DateHourSelectDialog(
    var dateList: ArrayList<String>,
    var hourList: ArrayList<ArrayList<String>>
) :
    DialogFragment() {
    private var _binding: DialogDateHourSelectBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateAdapter: DateHourSelectAdapter
    private lateinit var hourAdapter: DateHourSelectAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDateHourSelectBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    } // End of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateAdapter = DateHourSelectAdapter(dateList)
        hourAdapter = DateHourSelectAdapter(hourList[0])

        initRecyclerView()

        binding.okButton.setOnClickListener {
            val dateIdx = binding.dateRecyclerview.getSelectedPosition()
            val hourIdx = binding.hourRecyclerview.getSelectedPosition()
            val auroraFragment = parentFragment as AuroraFragment
            auroraFragment.changeDateTime(
                dateList[dateIdx],
                hourList[dateIdx][hourIdx]
            )
            dismiss()
        }
    } // End of onViewCreated

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    } // End of onDestroyView

    private fun initRecyclerView() {
        binding.dateRecyclerview.apply {
            adapter = dateAdapter
            setAlpha(true)
            setIntervalRatio(0.8f)
            setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                override fun onItemSelected(position: Int) {
                    hourAdapter.itemList = hourList[position]
                    hourAdapter.notifyDataSetChanged()
                }
            })
        }

        binding.hourRecyclerview.apply {
            adapter = hourAdapter
            setAlpha(true)
            setIntervalRatio(0.8f)
            setOrientation(RecyclerView.VERTICAL)
        }
    } // End of initRecyclerView
} // End of DateHourSelectDialog