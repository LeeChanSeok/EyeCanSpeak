package com.nassafy.aro.ui.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentJoinPasswordBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.login.viewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class JoinPasswordFragment :
    BaseFragment<FragmentJoinPasswordBinding>(FragmentJoinPasswordBinding::inflate) {

    private val loginActivityViewModel: LoginActivityViewModel by activityViewModels()
    private var password: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    } // End of onViewCreated

    override fun onResume() {
        super.onResume()
        setNextButtonAvailable(false)
    }

    private fun setNextButtonAvailable(isAvailable: Boolean) {
        binding.nextButton.isSelected = isAvailable
        binding.nextButton.isEnabled = isAvailable
        binding.nextButton.isClickable = isAvailable
    } // End of setNextButtonAvalable

    private fun initView() {
        binding.nextButton.setOnClickListener {
            loginActivityViewModel.password =
                binding.joinPasswordTextfield.editText?.text.toString()
            findNavController().navigate(R.id.action_joinPasswordFragment_to_joinNicknameFragment)
        }
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.joinPasswordTextfield.apply {
            editText?.addTextChangedListener(object : TextWatcher {
                var job: Job? = null
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    job?.cancel()
                    job = CoroutineScope(Dispatchers.Main).launch {
                        delay(300)
                        validateCheck()
                    }
                }

                override fun afterTextChanged(text: Editable?) {} // End of afterTextChanged
            }) // End of addTextChangedListener
        }
        binding.verificationPasswordTextfield.apply {
            editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(text: Editable?) {
                    validateCheck()
                } // End of afterTextChanged
            }) // End of addTextChangedListener
        }
    }

    private fun validateCheck() {
        val originPasswordTextField = binding.joinPasswordTextfield
        val originPassword = originPasswordTextField.editText?.text.toString()
        val verificationPasswordTextField = binding.verificationPasswordTextfield
        val verificationPassword = verificationPasswordTextField.editText?.text.toString()

        val result: Boolean = when (originPassword == verificationPassword) {
            true -> { // 같을 경우
                verificationPasswordTextField.error = null
                when (originPassword.length) {
                    0 -> {
                        originPasswordTextField.error = null
                        false
                    }
                    else -> {
                        when (isPasswordValid(originPassword)) {
                            true -> {
                                originPasswordTextField.error = null
                                true
                            }
                            false -> {
                                originPasswordTextField.error =
                                    getString(R.string.password_rule_fail_textview_text)
                                false
                            }
                        }
                    }
                }
            }
            false -> { // 다를 경우
                when (originPassword.length) {
                    0 -> {
                        originPasswordTextField.error = null
                        verificationPasswordTextField.error =
                            getString(R.string.password_validate_fail_textview_text)
                    }
                    else -> {
                        originPasswordTextField.error =
                            getString(R.string.password_rule_fail_textview_text)
                        when (verificationPassword.length) {
                            0 -> {
                                verificationPasswordTextField.error = null
                                when (isPasswordValid(originPassword)) {
                                    true -> originPasswordTextField.error = null
                                    false -> {}
                                }
                            }
                            else -> {
                                verificationPasswordTextField.error =
                                    getString(R.string.password_validate_fail_textview_text)
                                when (isPasswordValid(originPassword)) {
                                    true -> originPasswordTextField.error = null
                                    false -> {}
                                }
                            } // End of 비밀번호 확인 길이가 0이 아닌 경우
                        } // End of when (비밀번호 확인 길이)
                    } // End of 비밀번호 길이가 0이 아닐 경우
                } // End of when 비밀번호 길이
                false
            } // End of 둘이 같지 않을 경우
        } // End of when

        setNextButtonAvailable(result)
    } // End of validateCheck

    private fun passwordValidCheck(password: String): Boolean {
        return when (password.length in 8..15) {
            true -> {
                when (isPasswordValid(password)) {
                    true -> true
                    else -> false
                } // End of when
            } // ENd of true
            false -> false
        } // End of when
    } // End of emailValidCheck

    /*
        비밀번호 규칙
        1. 8자리 이상 15자리 이하.
        2. 숫자, 문자, 특수문자 모두 포함
     */
    private companion object {
        @JvmStatic
        val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$).*$"""
        fun isPasswordValid(pw: String): Boolean {
            return PASSWORD_REGEX.toRegex().matches(pw)
        } // End of isPasswordValid
    } // End of private companion object
}