package com.nassafy.aro.ui.view.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.databinding.FragmentAroServiceSelectBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.login.viewmodel.JoinServiceFragmentViewModel
import com.nassafy.aro.ui.view.login.viewmodel.LoginActivityViewModel
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinServiceFragment : BaseFragment<FragmentAroServiceSelectBinding>(FragmentAroServiceSelectBinding::inflate) {

    private val loginActivityViewModel: LoginActivityViewModel by activityViewModels()
    private val joinSericeFragmentViewModel: JoinServiceFragmentViewModel by viewModels()
    override fun onResume() {
        super.onResume()
        binding.auroraServiceCardview.setIsSelected(loginActivityViewModel.isAuroraServiceSelected)
        binding.meteorServiceCardview.setIsSelected(loginActivityViewModel.isMeteorServiceSelected)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initView()
    }

    private fun initObserve() {

        joinSericeFragmentViewModel.userJoinNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (loginActivityViewModel.placeListLiveData.value!!) {
                is NetworkResult.Success<List<PlaceItem>> -> {
                    requireView().showSnackBarMessage("회원가입 성공!")
                    findNavController().navigate(R.id.action_joinServiceFragment_to_loginFragment)
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

    }

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
        binding.serviceSelectSkipTextview.setOnClickListener {
            loginActivityViewModel.apply {
                joinSericeFragmentViewModel.join(UserTest(
                    email = email,
                    password = password,
                    nickname = nickname,
                    alarm = true,
                    auroraService = false,
                    auroraPlaces = emptyList(),
                    meteorService = false,
                    meteorPlaces = emptyList(),
                ))
            }
        }
    }

}