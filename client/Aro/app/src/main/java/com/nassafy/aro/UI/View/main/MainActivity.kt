package com.nassafy.aro.ui.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.ActivityMainBinding
import com.nassafy.aro.service.AroFCM
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainActivity_싸피"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragmentcontainerview) as NavHostFragment
        val navController = navHostFragment.navController

        binding.mainNavigation.setupWithNavController(navController)

        initObserver()
        initDrawer()
        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.getUserInfo()
        }


        /*
            JWT토큰을 보내서 회원정보를 가져오는데,Body에 FCM토큰을 담아서 요청을 보냄
         */
        callFcmToken()


    } // End of onCreate

    private fun callFcmToken(): String {
        return AroFCM().getFirebaseToken()
    } // End of callFcmToken

    private fun initObserver() {
        mainActivityViewModel.userInfo.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    Log.d("ssafy/memberInfo/result", it.data.toString())
                    mainActivityViewModel.email = it.data!!.email
                    mainActivityViewModel.nickname = it.data!!.nickname
                }
                is NetworkResult.Error -> {
                    binding.root.showSnackBarMessage("유저 정보를 불러오는데 실패했습니다.")
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun initDrawer() {
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.mainDrawerlayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                val nickname: TextView = findViewById<View>(R.id.nickname_textview) as TextView
                val email: TextView = findViewById<View>(R.id.email_textview) as TextView
                val closeButton: ImageButton = findViewById(R.id.close_button)

                // TODO : Add Nickname and Email from User Data
//                nickname.text = user.nickname
//                email.text = user.email

                closeButton.setOnClickListener {
                    closeDrawer()
                }
            }
        }
        binding.mainDrawerlayout.apply {
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            addDrawerListener(toggle)
        }
        toggle.syncState()
    } // End of initDrawer

    // Main Fragment에서 버튼을 누르면 Drawer Open
    fun openDrawer() {
        binding.mainDrawerlayout.openDrawer(GravityCompat.END)
    }

    fun closeDrawer() {
        Log.d(TAG, "closeDrawer: close 버튼 활성화됨")
        binding.mainDrawerlayout.closeDrawer(GravityCompat.END)
    }

} // End of MainActivity class
