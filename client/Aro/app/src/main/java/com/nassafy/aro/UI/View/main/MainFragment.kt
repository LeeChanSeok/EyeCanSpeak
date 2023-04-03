package com.nassafy.aro.ui.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentMainBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.meteorshower.MeteorShowerFragment
import com.nassafy.aro.ui.view.aurora.AuroraFragment

private const val TAG = "MainFragment_싸피"

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private lateinit var mContext: Context
    private var tabIcons: ArrayList<View> = arrayListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun checkPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    Log.d(TAG, "onPermissionGranted: 권한이 허용됨")

                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Log.d(TAG, "onPermissionDenied: 권한이 허용되지 않음")
                }
            })
            .setDeniedMessage("갤러리 권한을 혀용해주세요")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    } // End of checkPermission

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //checkPermission()

        binding.viewpager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
            isUserInputEnabled = false
        }
        // initialize tab layout
        initTabLayout()

    } // End of onViewCreated

    private fun initTabLayout() {
        // add tablayout Icon with using custom view
        val tabIcon1 = layoutInflater.inflate(R.layout.tab_icon_custom_view, null)
        tabIcon1.findViewById<ImageView>(R.id.tabicon_imageview)
            .setBackgroundResource(R.drawable.tab_aurora_icon)
        tabIcon1.findViewById<TextView>(R.id.tabicon_textview).text =
            getString(R.string.service_aurora)
        val tabIcon2 = layoutInflater.inflate(R.layout.tab_icon_custom_view, null)
        tabIcon2.findViewById<ImageView>(R.id.tabicon_imageview)
            .setBackgroundResource(R.drawable.tab_meteor_shower_icon)
        tabIcon2.findViewById<TextView>(R.id.tabicon_textview).text =
            getString(R.string.service_meteor_shower)

        tabIcons = arrayListOf(tabIcon1, tabIcon2)

        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.customView = tabIcons[position]
        }.attach()
    } // End of initTabLayout

    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return tabIcons.size
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AuroraFragment()
                // TODO: Meteor Shower Fragment
                else -> MeteorShowerFragment()
            }
        }
    } // End of ViewPagerAdapter
}