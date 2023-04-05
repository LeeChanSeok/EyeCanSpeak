package com.nassafy.aro.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nassafy.aro.Application
import com.nassafy.aro.BuildConfig.NAVER_CLIENT_ID
import com.nassafy.aro.BuildConfig.NAVER_CLIENT_SECRET
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentLoginBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.login.viewmodel.LoginActivityViewModel
import com.nassafy.aro.ui.view.login.viewmodel.LoginFragmentViewModel
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.githubLoginUri
import com.nassafy.aro.util.showSnackBarMessage
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val loginActivityViewModel: LoginActivityViewModel by activityViewModels()
    private val loginFragmentViewModel: LoginFragmentViewModel by viewModels()
    private var isTriedLoginState = false
    private var finishFlag = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()
        when (loginActivityViewModel.isTriedGithubLogin) {
            true -> {
                loginActivityViewModel.isTriedGithubLogin = false
                isTriedLoginState = true
                Log.d("ssafy/github/code", loginActivityViewModel.githubCode)
                CoroutineScope(Dispatchers.Main).launch {
                    snsLogin("GITHUB", "")
                }
            }
            false -> {}
        }
        initView()
    } // End of onViewCreated

    override fun onResume() {
        super.onResume()
        isTriedLoginState = false
    } // End of onResume

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            when (finishFlag) {
                true -> {
                    requireActivity().finish()
                }
                false -> {
                    finishFlag = true
                    CoroutineScope(Dispatchers.IO).launch {
                        requireView().showSnackBarMessage("\'뒤로가기\' 버튼을 한번 더 누르시면 종료됩니다.")
                        delay(1800)
                        finishFlag = false
                    }
                }
            }
        }
    } // End of onCreate

    private fun naverLogin() {
        val oAuthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    override fun onSuccess(result: NidProfileResponse) {
                        val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                        naverAccessToken ?: return
                        val providerType = "NAVER"
                        Log.d("ssafy/auth/naver", "naverAccessToken: $naverAccessToken")
                        snsLogin(providerType, naverAccessToken)
                    } // End of onSuccess

                    override fun onError(errorCode: Int, message: String) {
                        NidOAuthLogin().callDeleteTokenApi(
                            requireContext(),
                            object : OAuthLoginCallback {
                                override fun onError(errorCode: Int, message: String) {
                                    onFailure(errorCode, message)
                                }

                                override fun onFailure(httpStatus: Int, message: String) {
                                }

                                override fun onSuccess() {
                                }

                            })

                        requireView().showSnackBarMessage("네이버 로그인에 실패했습니다.")
                        binding.progressbar.isVisible = false
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        // Todo loading progressBar
                    }
                })
            }

            override fun onError(errorCode: Int, message: String) {
                requireView().showSnackBarMessage("네이버 로그인에 실패했습니다.")
                binding.progressbar.isVisible = false
            }

            override fun onFailure(httpStatus: Int, message: String) {
                requireView().showSnackBarMessage("네이버 로그인에 실패했습니다.")
                binding.progressbar.isVisible = false
            }
        }
        NaverIdLoginSDK.initialize(requireContext(), NAVER_CLIENT_ID, NAVER_CLIENT_SECRET, "ARO")
        NaverIdLoginSDK.authenticate(requireContext(), oAuthLoginCallback)
    } // End of naverLogin

    fun githubLogin() {
        startActivity(Intent(Intent.ACTION_VIEW, githubLoginUri).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        })
    } // End of githubLogin

    private fun snsLogin(providerType: String, accessToken: String) {
        binding.progressbar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            loginFragmentViewModel.apply {
                when (providerType) {
                    "NAVER" -> {
                        snsLogin(providerType, accessToken)
                    }
                    "GITHUB" -> {
                        snsLogin(
                            providerType,
                            loginFragmentViewModel.getAccessToken(loginActivityViewModel.githubCode)
                        )
                    }
                    else -> {
                        return@launch
                    }
                }
                val networkResult = userSnsLoginNetworkResultLiveData.value
                Log.d("ssafy/snslogin/networkResult", networkResult?.data.toString())
                launch(Dispatchers.Main) {
                    when (networkResult) {
                        is NetworkResult.Success -> {
                            when (networkResult.data!!.signup) {
                                true -> {
                                    loginByIdPassword(
                                        networkResult.data!!.providerType,
                                        networkResult.data!!.email,
                                        null
                                    )
                                }
                                false -> {
                                    loginActivityViewModel.providerType =
                                        networkResult.data!!.providerType
                                    loginActivityViewModel.email =
                                        networkResult.data!!.email
                                    findNavController().navigate(R.id.action_loginFragment_to_joinNicknameFragment)
                                }
                            }
                        }
                        is NetworkResult.Error -> {
                            requireView().showSnackBarMessage("$providerType 로그인에 실패했습니다.")
                            binding.progressbar.isVisible = false
                        }
                        is NetworkResult.Loading -> {
                            binding.progressbar.isVisible = true
                        }
                        else -> {
                            requireView().showSnackBarMessage("$providerType 로그인에 실패했습니다.")
                        }
                    } // End of when
                }
            }
        }
    }

    private fun initObserve() {

        loginFragmentViewModel.loginToken.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data
                    Log.d("ssafy/login", it.data.toString())
                    Application.sharedPreferencesUtil.addUserAccessToken(data?.accessToken ?: "")
                    Application.sharedPreferencesUtil.addUserRefreshToken(data?.refreshToken ?: "")
                    startMainActivity()
                }
                is NetworkResult.Error -> {
                    binding.progressbar.isVisible = false
                    when (isTriedLoginState) {
                        true -> {
                            requireView().showSnackBarMessage("로그인에 실패했습니다.")
                            binding.loginEmailIdEdittext.error = " "
                            binding.loginPasswordEdittext.error = " "

                            CoroutineScope(Dispatchers.Main).launch {
                                delay(1000)
                                binding.loginEmailIdEdittext.error = null
                                binding.loginPasswordEdittext.error = null
                            }
                        }
                        false -> {}
                    }

                }
                is NetworkResult.Loading -> {
                    when (isTriedLoginState) {
                        true -> {
                            binding.progressbar.isVisible = true
                        }
                        false -> {}
                    }
                }
            }
            isTriedLoginState = false
        }
    } // End of initObserve

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun initView() {
        binding.joinTextview.setOnClickListener {
            loginActivityViewModel.providerType = "LOCAL"
            findNavController().navigate(R.id.action_loginFragment_to_joinEmailFragment)
        }
        binding.loginNaverImagebutton.setOnClickListener {
            isTriedLoginState = true
            naverLogin()
        }
        binding.loginGithubImagebutton.setOnClickListener {
            isTriedLoginState = true
            githubLogin()
        }
        binding.loginButton.setOnClickListener {
            when (binding.loginEmailIdEdittext.editText?.text.toString().trim().length) {
                0 -> {
                    binding.loginEmailIdEdittext.error = getString(R.string.email_empty_text)
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        binding.loginEmailIdEdittext.error = null
                        binding.loginPasswordEdittext.error = null
                    }
                    when (binding.loginPasswordEdittext.editText?.text.toString().trim().length) {
                        0 -> {}
                        else -> {
                            return@setOnClickListener
                        }
                    }
                }
                else -> {}
            }
            when (binding.loginPasswordEdittext.editText?.text.toString().trim().length) {
                0 -> {
                    binding.loginPasswordEdittext.error = getString(R.string.password_empty_text)
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        binding.loginEmailIdEdittext.error = null
                        binding.loginPasswordEdittext.error = null
                    }
                    return@setOnClickListener
                }
                else -> {}
            }

            isTriedLoginState = true
            CoroutineScope(Dispatchers.IO).launch {
                loginFragmentViewModel.loginByIdPassword(
                    "LOCAL",
                    binding.loginEmailIdEdittext.editText?.text.toString(),
                    binding.loginPasswordEdittext.editText?.text.toString(),
                )
            }

        }
    } // ENd of initView

}