package com.nassafy.aro.ui.view.main

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.ActivityMainBinding
import com.nassafy.aro.service.AroFCM
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import com.nassafy.aro.util.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainActivity_싸피"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Activity ViewModel
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    // 런타임 권한 요청시 필요한 요청 코드
    private val PERMISSION_REQUEST_CODE = 100

    // 권한 목록
    var REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    // 위치 서비스 요청 시 필요한 런처
    lateinit var getGPSPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragmentcontainerview) as NavHostFragment
        val navController = navHostFragment.navController

        binding.mainNavigation.setupWithNavController(navController)

        initObserver()
        initDrawer()

        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.getUserInfo(callFcmToken())
        }
    } // End of onCreate

    private fun checkPermission() {
        // 1. GPS가 켜져 있는지를 확인
        if (!isLocationServicesAvailable()) {
            // GPS가 켜져있지 않다면 dialog창을 띄워서 위치설정 할 수 있는 다이얼로그 창을 띄움
            showDialogForLocationServiceSetting()
        } else {
            // GPS가 켜져있을 때는 위치 권한을 가지고 있는지를 확인
            isRunTimePermissionsGranted()
        }
    } // End of checkPermission

    private fun isRunTimePermissionsGranted() {
        // 위치 권한이 있는지 확인
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // 알림은 받지 않아도 크게 상관없긴 한데 고민됨.
        val hasNotificationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            android.Manifest.permission.POST_NOTIFICATIONS
        )

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
            hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ||
            hasNotificationPermission != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                REQUIRED_PERMISSIONS,
                PERMISSION_REQUEST_CODE
            )
        }
    } // End of isRunTimePermissionsGranted

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
            var checkResult = true

            // 모든 권한을 허용했는지 확인
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false
                    break
                }
            }

            if (!checkResult) {
                Log.d(TAG, "onRequestPermissionsResult: 일부 권한이 허용되지 않았음")
            }
        }
    } // End of onRequestPermissionsResult


    private fun isRunTimePermissoinsGranted() {
        // 위치 권한이 있는지 확인
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

    } // End of isRunTimePermissionsGranted

    private fun showDialogForLocationServiceSetting() {
        getGPSPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (isLocationServicesAvailable()) {
                    isRunTimePermissionsGranted()
                } else {
                    this.showToastMessage("위치 서비스를 사용할 수 없습니다.")
                    finish()
                }
            }
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.run {
            setTitle("위치 서비스 비활성화")
            setMessage("위치 서비스가 꺼져 있습니다, 설정 후 사용가능합니다.")
            setCancelable(true)

            // 확인 버튼 설정
            setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
                DialogInterface.OnClickListener { dialog, id ->
                    val callGPSSettingIntent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    getGPSPermissionLauncher.launch(callGPSSettingIntent)
                }
            })


            // 취소 버튼 설정
            setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                context.showToastMessage("기기에서 위치서비스를 설정 후 사용해주세요")
                finish()
            })
            create().show() // 다이얼로그 생성
        }
    } // End of showDialogForLocationServiceSetting


    private fun callFcmToken(): String {
        return AroFCM().getFirebaseToken()
    } // End of callFcmToken


    private fun initObserver() {
        mainActivityViewModel.userInfo.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
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
    } // End of openDrawer

    fun closeDrawer() {
        binding.mainDrawerlayout.closeDrawer(GravityCompat.END)
    } // End of closeDrawer

    private fun isLocationServicesAvailable(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        ))
    } // End of isLocationServicesAvailable
} // End of MainActivity class
