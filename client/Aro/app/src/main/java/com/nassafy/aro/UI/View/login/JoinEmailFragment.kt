package com.nassafy.aro.ui.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentJoinEmailBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.login.viewmodel.JoinEmailFragmentViewModel
import com.nassafy.aro.ui.view.login.viewmodel.LoginActivityViewModel
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class JoinEmailFragment :
    BaseFragment<FragmentJoinEmailBinding>(FragmentJoinEmailBinding::inflate) {

    private var isVerifyEmailTextviewClicked = false
    private val loginActivityViewModel: LoginActivityViewModel by activityViewModels()
    private val joinEmailFragmentViewModel: JoinEmailFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initView()
    } // End of onViewCreated

    override fun onResume() {
        super.onResume()
        isVerifyEmailTextviewClicked = false
        setNextButtonAvailable(false)
    } // End of onResume

    private fun initObserve() {
        initEmailValidateObserve()
    } // End of initObserve

    private fun setNextButtonAvailable(isAvailable: Boolean) {
        binding.nextButton.isSelected = isAvailable
        binding.nextButton.isEnabled = isAvailable
        binding.nextButton.isClickable = isAvailable
    } // End of setNextButtonAvalable

    private fun initEmailValidateObserve() {
        joinEmailFragmentViewModel.isEmailValidated.observe(this.viewLifecycleOwner) {
            Log.d("ssafy_pcs", "progress.Off")
            when (it) {
                is NetworkResult.Success -> {
                    isVerifyEmailTextviewClicked = false
                    binding.progressGroup.isGone = true
                    when(it.data!!){
                        true -> {
                            binding.joinEmailIdTextfield.error = null
                            binding.verificationEmailCodeTextfield.isVisible = true
                        }
                        false -> {
                            binding.joinEmailIdTextfield.error =
                                getString(R.string.join_already_exist_email_text)
                            binding.verificationEmailCodeTextfield.isVisible = false
                        }
                    }
                }
                is NetworkResult.Error -> {
                    isVerifyEmailTextviewClicked = false
                    binding.progressGroup.isGone = true
                    requireView().showSnackBarMessage(getString(R.string.join_already_exist_email_text))
                }
                is NetworkResult.Loading -> {
                    Log.d("ssafy_pcs", "progress.On")
                    when (isVerifyEmailTextviewClicked) {
                        true -> {binding.progressGroup.isVisible = true}
                        false -> {}
                    }
                }
            } // End of when
        } // End of isEmailValidated.observe
        joinEmailFragmentViewModel.isEmailAuthCodeValidated.observe(this.viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.verificationEmailCodeTextfield.error = null
                    when (joinEmailFragmentViewModel.isEmailValidated.value?.data) {
                        true -> {
                            setNextButtonAvailable(true)
                        }
                        else -> {
                            setNextButtonAvailable(false) // TODO ACTIVE
                        }
                    } // End of when
                } // End of true
                false -> {
//                    setNextButtonAvailable(true)//TODO DELETE
                    setNextButtonAvailable(false) // TODO ACTIVE
                    binding.verificationEmailCodeTextfield.error =
                        getString(R.string.email_validate_number_fail_textview_text)
                } // End of false
            } // End of when
        } // End of isEmailValidated.observe
    } // End of initEmailValidateObserve

    private fun initView() {
        binding.nextButton.apply {
            setOnClickListener {
                loginActivityViewModel.email =
                    binding.joinEmailIdTextfield.editText?.text.toString()
                findNavController().navigate(R.id.action_joinEmailFragment_to_joinPasswordFragment)
            } // End of nextButton.setOnClickListener
        } // End of nextButton.apply
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        } // End of cancelButton.setOnClickListener
        binding.verifyEmailTextview.setOnClickListener {
            isVerifyEmailTextviewClicked = true
            CoroutineScope(Dispatchers.IO).launch {
                joinEmailFragmentViewModel.validateEmail(binding.joinEmailIdTextfield.editText?.text.toString())
            }
            when (isVerifyEmailTextviewClicked) {
                true -> {binding.progressGroup.isVisible = true}
                false -> {}
            }
        } // End of verifyEmailTextview.setOnClickListener

        binding.joinEmailIdTextfield.apply {
            editText?.setText(loginActivityViewModel.email)
            editText?.addTextChangedListener {
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(p0: Editable?) {
                        binding.verificationEmailCodeTextfield.editText?.setText("")
                        binding.verificationEmailCodeTextfield.isGone = true
                    }
                }
            } // End of addTextChangedListener
        } // End of joinEmailIdTextfield.apply

        binding.verificationEmailCodeTextfield.apply {
            editText?.setText("")
            editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(text: Editable?) {
                    when (text?.length) {
                        6 -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                joinEmailFragmentViewModel.validateEmialAuthCode(
                                    binding.joinEmailIdTextfield.editText?.text.toString(),
                                    text.toString()
                                ) // End of return
                            } // End of CoroutineScope
                        } // End of case 6
                        else -> {
                            binding.verificationEmailCodeTextfield.error = ""
                        } // End of case else
                    } // End of text length
                } // End of afterTextChanged
            }) // End of addTextChangedListener
        } // End of verificationEmailCodeTextfield.apply
    } // End of initView
} // End of JoinEmailFragment