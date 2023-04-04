package com.nassafy.aro.ui.view.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentAroServiceSelectBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.dialog.OkCancelDialog
import com.nassafy.aro.ui.view.main.MainActivityViewModel
import com.nassafy.aro.ui.view.main.mypage.viewmodel.MyPageServiceRegisterFragmentViewModel
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageServiceRegisterFragment :
    BaseFragment<FragmentAroServiceSelectBinding>(FragmentAroServiceSelectBinding::inflate) {

    private val myPageServiceRegisterFragementViewModel: MyPageServiceRegisterFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MyPageServiceRegisterFragmentArgs by navArgs()
        myPageServiceRegisterFragementViewModel.auroraService = args.auroraService
        myPageServiceRegisterFragementViewModel.meteorService = args.meteorService

        initObserve()
        initView()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initObserve() {

        myPageServiceRegisterFragementViewModel.setSelectServiceNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    Log.d("ssafy", "success")
                    mainActivityViewModel.auroraServiceEnabled = myPageServiceRegisterFragementViewModel.auroraService
                    mainActivityViewModel.meteorShowerServiceEnabled = myPageServiceRegisterFragementViewModel.meteorService

                    requireView().showSnackBarMessage(getString(R.string.service_modify_success_text))
                    findNavController().navigate(R.id.action_myPageServiceRegisterFragment_to_myPageFragment)
                }
                is NetworkResult.Error -> {
                    Log.d("ssafy", "error")

                }
                is NetworkResult.Loading -> {
                    Log.d("ssafy", "loading")
                }
            }
        }
    }

    private fun initView() {
        binding.serviceSelectLaterGroup.isGone = true
        binding.auroraServiceCardview.setIsSelected(myPageServiceRegisterFragementViewModel.auroraService)
        binding.meteorServiceCardview.setIsSelected(myPageServiceRegisterFragementViewModel.meteorService)

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.nextButton.setOnClickListener {
            val okCancelDialog = OkCancelDialog(
                getString(R.string.service_modify_no_have_service_dialog_title_textview_text),
                getString(R.string.service_modify_no_have_service_dialog_inform_textview_text),
                object : OkCancelDialog.SetOnOkButtonClickListener {
                    override fun onOkButtonClick() {
                        serviceSelect()
                    } // End of onOkButtonClick
                } // End of SetOnOkButtonClickListener
            )
            // true -> false 있는 경우
            when ((myPageServiceRegisterFragementViewModel.auroraService == true &&
                    binding.auroraServiceCardview.getIsSelected() == false) ||
                    (myPageServiceRegisterFragementViewModel.meteorService == true &&
                            binding.meteorServiceCardview.getIsSelected() == false)) {
                true -> {
                    okCancelDialog.show(childFragmentManager, "OkCancelDialog")
                }
                false -> {
                    serviceSelect()
                }
            }
        } // End of nextButton.setOnClickListener
    } // End of initView

    fun serviceSelect() {
        myPageServiceRegisterFragementViewModel.auroraService =
            binding.auroraServiceCardview.getIsSelected()
        myPageServiceRegisterFragementViewModel.meteorService =
            binding.meteorServiceCardview.getIsSelected()
        val action =
            MyPageServiceRegisterFragmentDirections.actionMyPageServiceRegisterFragmentToMyPageFavoriteRegisterFragment(
                myPageServiceRegisterFragementViewModel.auroraService,
                myPageServiceRegisterFragementViewModel.meteorService,
            )
        when (myPageServiceRegisterFragementViewModel.auroraService || myPageServiceRegisterFragementViewModel.meteorService) {
            true -> {
                findNavController().navigate(action)
            }
            false -> {
                CoroutineScope(Dispatchers.IO).launch {
                    myPageServiceRegisterFragementViewModel.selectService(
                        myPageServiceRegisterFragementViewModel.auroraService,
                        myPageServiceRegisterFragementViewModel.meteorService
                    ) // End of selectService
                } // End of CoroutineScope
            } // End of false
        } // End of when
    }
} // End of MyPageServiceRegisterFragment