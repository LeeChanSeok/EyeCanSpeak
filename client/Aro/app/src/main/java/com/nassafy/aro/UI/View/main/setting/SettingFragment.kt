package com.nassafy.aro.ui.view.main.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentSettingBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.dialog.OkCancelDialog
import com.nassafy.aro.ui.view.login.LoginActivity
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.ui.view.main.MainActivityViewModel
import com.nassafy.aro.ui.view.main.mypage.MyPageServiceRegisterFragmentDirections
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import com.nassafy.aro.util.showToastView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val settingFragmentViewModel: SettingFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val main = requireActivity() as MainActivity
        main.closeDrawer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()
        initView()

    } // End of onViewCreated

    private fun initObserve() {
        settingFragmentViewModel.getAlarmOptionNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    activityViewModel.alarmOption = it.data!!
                    binding.settingsFragmentNotificationSetToogleImgaeButton.isSelected =
                        activityViewModel.alarmOption
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("네트워크 통신 에러")
                }
                is NetworkResult.Loading -> {}
            }
        } // End of getAlarmOptionNetworkResultLiveData observe
        settingFragmentViewModel.setAlarmOptionNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    activityViewModel.alarmOption = !activityViewModel.alarmOption
                    binding.settingsFragmentNotificationSetToogleImgaeButton.isSelected =
                        activityViewModel.alarmOption
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("네트워크 통신 에러")
                }
                is NetworkResult.Loading -> {}
            }
        } // End of setAlarmOptionNetworkResultLiveData observe
        settingFragmentViewModel.getAuroraOptionNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    activityViewModel.auroraDisplayOption = it.data!!
                    binding.settingsFragmentMapOnAuroraSetToogleImgaeButton.isSelected =
                        activityViewModel.auroraDisplayOption
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("네트워크 통신 에러")
                }
                is NetworkResult.Loading -> {}
            }
        } // End of getAuroraOptionNetworkResultLiveData observe
        settingFragmentViewModel.setAuroraOptionNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    activityViewModel.auroraDisplayOption = !activityViewModel.auroraDisplayOption
                    binding.settingsFragmentMapOnAuroraSetToogleImgaeButton.isSelected =
                        activityViewModel.auroraDisplayOption
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("네트워크 통신 에러")
                }
                is NetworkResult.Loading -> {}
            }
        } // End of setAuroraOptionNetworkResultLiveData observe

        settingFragmentViewModel.getCloudOptionNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    activityViewModel.cloudDisplayOption = it.data!!
                    binding.settingsFragmentMapOnAuroraSetToogleImgaeButton.isSelected =
                        activityViewModel.cloudDisplayOption
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("네트워크 통신 에러")
                }
                is NetworkResult.Loading -> {}
            }
        } // End of getCloudOptionNetworkResultLiveData observe
        settingFragmentViewModel.setCloudOptionNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    activityViewModel.cloudDisplayOption = !activityViewModel.cloudDisplayOption
                    binding.settingsFragmentMapOnCloudSetToogleImgaeButton.isSelected =
                        activityViewModel.cloudDisplayOption
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("네트워크 통신 에러")
                }
                is NetworkResult.Loading -> {}
            }
        } // End of setCloudOptionNetworkResultLiveData observe

        settingFragmentViewModel.deleteAccountNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val layout = layoutInflater.inflate(R.layout.custom_toast_delete_account, null)
                    requireContext().showToastView(layout)

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("네트워크 통신 에러")
                }
                is NetworkResult.Loading -> {}
            }
        } // End of deleteAccountNetworkResultLiveData observe
    } // End of initObserve

    private fun initView() {
        CoroutineScope(Dispatchers.IO).launch {
            settingFragmentViewModel.apply {
                getAlarmOption()
                getAuroraDisplayOption()
                // getCloudDisplayOption()
            }
        }

        binding.settingsFragmentNotificationSetToogleImgaeButton.setOnClickListener {
            settingFragmentViewModel.setAlarmOption()
        }

        binding.settingsFragmentMapOnAuroraSetToogleImgaeButton.setOnClickListener {
            settingFragmentViewModel.setAuroraDisplayOption()
        }

        binding.settingsFragmentMapOnCloudSetToogleImgaeButton.setOnClickListener {
            settingFragmentViewModel.setCloudDisplayOption()
        }

        binding.settingsFragmentDeleteAccountImgaeButton.setOnClickListener {
            val okCancelDialog = OkCancelDialog(
                getString(R.string.delete_account_dialog_title_textview_text),
                getString(R.string.delete_account_dialog_inform_textview_text),
                object : OkCancelDialog.SetOnOkButtonClickListener {
                    override fun onOkButtonClick() {
                            settingFragmentViewModel.deleteAccount(activityViewModel.email)
                    }
                }
            )
            okCancelDialog.show(childFragmentManager, "OkCancelDialog")
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

    } // End of initView

} // End of SettingFragment