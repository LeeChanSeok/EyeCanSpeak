package com.nassafy.aro.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentAuroraBinding
import com.nassafy.aro.databinding.FragmentMeteorShowerBinding
import com.nassafy.aro.ui.view.main.MainActivity

class MeteorShowerFragment : BaseFragment<FragmentMeteorShowerBinding>(FragmentMeteorShowerBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.drawerImagebutton.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.openDrawer()
        }

        binding.meteorShowerTextview.text = getString(R.string.meteor_not_ready_text)
    }

}