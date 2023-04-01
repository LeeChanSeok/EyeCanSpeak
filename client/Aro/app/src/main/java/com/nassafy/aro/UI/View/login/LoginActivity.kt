package com.nassafy.aro.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.ActivityLoginBinding
import com.nassafy.aro.service.AroFCM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AroFCM().getFirebaseToken()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    } // End of onNewIntent

    override fun onResume() {
        super.onResume()
        val uri = intent?.data
        // Oauth 로그인 인지 아닌지 확인
        when (uri?.scheme.toString()) {
            "aro-github" -> { // 깃허브
                val code = uri?.getQueryParameter("code")
                // TODO Delete Log
                Log.d("ssafy/auth/github/code", "$code")
                //TODO Get Github AccessToken, To use Retrofit
                binding.navHost.findNavController().navigate(R.id.action_splashFragment_to_LoginFragment)
            }
            else -> {}
        } // End of when
    } // End of onResume

}