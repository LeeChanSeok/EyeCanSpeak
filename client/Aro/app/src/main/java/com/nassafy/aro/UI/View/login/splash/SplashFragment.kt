package com.nassafy.aro.ui.view.login.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentSplashBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.SharedPreferencesUtil
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    // Context
    private lateinit var mContext: Context

    // viewModel
    private val splashViewModel: SplashViewModel by viewModels()

    // sharedPreference
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    // Animation
    private lateinit var splash_top: android.view.animation.Animation
    private lateinit var splash_bottom: android.view.animation.Animation

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    } // End of onAttach

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    } // End of onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // JWT 토큰이 있는지 확인을 먼저하고,
        // 토큰이 있을 경우 서버에 보냄.
        postAccessTokenGetUserDataResponseLiveDataObserve()

        // For Animation
        splash_top = AnimationUtils.loadAnimation(mContext, R.anim.splash_top)
        splash_bottom = AnimationUtils.loadAnimation(mContext, R.anim.splash_bottom)

        CoroutineScope(Dispatchers.IO).launch {
            binding.logoImageview.animation = splash_top
            binding.splashLogoTextTextview.animation = splash_top
            binding.splashLogoTextTextview.animation = splash_top
            delay(2000)

            withContext(Dispatchers.Main) {
                binding.splashProgressbar.visibility = View.VISIBLE
                binding.splashProgressbar.isVisible = true
            }

            splashViewModel.postAccessTokenGetUserData()
        }
    } // End of onViewCreated


    /*
        토큰이 맞으면 화면을 전환함.
        AccessToken이 만료됬을 경우 RefreshToken을 줘서 다시 발급받음 RefreshToken도 만료됬으면 다시 로그인을 시킴
     */

    private fun postAccessTokenGetUserDataResponseLiveDataObserve() {
        splashViewModel.postAccessTokenGetUserDataResponseLiveData.observe(this.viewLifecycleOwner) {
            binding.splashProgressbar.visibility = View.GONE
            binding.splashProgressbar.isVisible = false

            // postAccessTokenGetUserDataResponseLiveData
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data == 200) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }

                is NetworkResult.Error -> {
                    // 토큰을 보내서 회원정보를 받아오는 시도를 함.
                    // 토큰 만료시 다시 로그인 페이지를 보여줌.
                    findNavController().navigate(R.id.action_splashFragment_to_LoginFragment)
                    requireView().showSnackBarMessage("토큰이 만료되었습니다 로그인을 다시 진행해주세요.")
                }

                is NetworkResult.Loading -> {
                    binding.splashProgressbar.visibility = View.VISIBLE
                    binding.splashProgressbar.isVisible = true
                }
            }
        }
    } // End of postAccessTokenGetUserDataResponseLiveDataObserve
} // End of SplashFragment
