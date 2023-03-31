package com.nassafy.aro.ui.view.login

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.nassafy.aro.Application
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.databinding.FragmentAroServiceSelectBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.login.viewmodel.JoinServiceFragmentViewModel
import com.nassafy.aro.ui.view.login.viewmodel.LoginActivityViewModel
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray

@AndroidEntryPoint
class JoinServiceFragment : BaseFragment<FragmentAroServiceSelectBinding>(FragmentAroServiceSelectBinding::inflate) {

    private val loginActivityViewModel: LoginActivityViewModel by activityViewModels()
    private val joinSericeFragmentViewModel: JoinServiceFragmentViewModel by viewModels()
    override fun onResume() {
        super.onResume()
        binding.auroraServiceCardview.setIsSelected(loginActivityViewModel.isAuroraServiceSelected)
        binding.meteorServiceCardview.setIsSelected(loginActivityViewModel.isMeteorServiceSelected)
    } // End of joinSericeFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initView()
    } // End of onViewCreated

    private fun initObserve() {

        joinSericeFragmentViewModel.userJoinNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (loginActivityViewModel.placeListLiveData.value!!) {
                is NetworkResult.Success<List<PlaceItem>> -> {
                    requireView().showSnackBarMessage("회원가입 성공!")
                    Application.sharedPreferencesUtil.addUserAccessToken(it.data?.accessToken ?: "")
                    Application.sharedPreferencesUtil.addUserRefreshToken(it.data?.refreshToken ?: "")
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                is NetworkResult.Error<*> -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }
                is NetworkResult.Loading<*> -> {
                    //TODO Loading
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                }
            }
        }

    } // End of initObserve

    private fun initView() {
        binding.nextButton.setOnClickListener {
            loginActivityViewModel.apply {
                isAuroraServiceSelected = binding.auroraServiceCardview.getIsSelected()
                isMeteorServiceSelected = binding.meteorServiceCardview.getIsSelected()
            }
            findNavController().navigate(R.id.action_joinServiceFragment_to_joinCountryPlaceSelectFragment)
        }
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.serviceSelectSkipTextview.apply {
            val gson = GsonBuilder().create()
            paintFlags = Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                loginActivityViewModel.apply {
                    joinSericeFragmentViewModel.join(JsonObject().apply {
                        addProperty("providerType", providerType)
                        addProperty("email", email)
                        addProperty("password", password)
                        addProperty("nickname", nickname)
                        addProperty("auroraService", false)
                        addProperty("auroraPlaces", JSONArray(emptyArray<PlaceItem>()).toString())
                        addProperty("meteorService", false)
                        addProperty("meteorPlaces", JSONArray(emptyArray<PlaceItem>()).toString())
                    })
                }
            }
        }
    } // End of initView

} // End of JoinServiceFragment