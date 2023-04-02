package com.nassafy.aro.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nassafy.aro.databinding.ActivityLoginBinding
import com.nassafy.aro.service.AroFCM
import com.nassafy.aro.ui.view.login.viewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val loginActivityViewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AroFCM().getFirebaseToken()
        val uri = intent?.data
        // Oauth 로그인 인지 아닌지 확인
        when (uri?.scheme.toString()) {
            "aro-github" -> { // 깃허브
                Log.d("ssafy/login/github/body", uri?.getQueryParameter("scope") ?: "")
                loginActivityViewModel.isTriedGithubLogin = true
                loginActivityViewModel.githubCode = uri?.getQueryParameter("code") ?: ""
            }
            else -> {}
        } // End of when
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    } // End of onNewIntent

    override fun onResume() {
        super.onResume()
//        val uri = intent?.data
//        // Oauth 로그인 인지 아닌지 확인
//        when (uri?.scheme.toString()) {
//            "aro-github" -> { // 깃허브
//                Log.d("ssafy/login/github", "onResume")
//                loginActivityViewModel.isTriedGithubLogin = true
//                loginActivityViewModel.githubToken = uri?.getQueryParameter("code") ?: ""
//            }
//            else -> {}
//        } // End of when
    } // End of onResume

}