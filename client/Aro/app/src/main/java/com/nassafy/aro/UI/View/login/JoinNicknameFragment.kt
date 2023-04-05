package com.nassafy.aro.ui.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentJoinNicknameBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.login.viewmodel.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinNicknameFragment :
    BaseFragment<FragmentJoinNicknameBinding>(FragmentJoinNicknameBinding::inflate) {

    private val loginActivityViewModel: LoginActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun setNextButtonAvailable(isAvailable: Boolean) {
        //TODO ACTIVATE
        binding.nextButton.isSelected = isAvailable
        binding.nextButton.isEnabled = isAvailable
        binding.nextButton.isClickable = isAvailable
    } // End of setNextButtonAvalable

    override fun onResume() {
        super.onResume()
        setNextButtonAvailable(false)
    }

    private fun initView() {

        binding.nextButton.setOnClickListener {
            loginActivityViewModel.nickname =
                binding.joinNicknameIdTextfield.editText?.text.toString()
            findNavController().navigate(R.id.action_joinNicknameFragment_to_joinServiceFragment)
        }
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.joinNicknameIdTextfield.apply {
            editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(text: Editable?) {
                    var result: Boolean =
                        when (text.toString().length in 3..10 || text.toString().isEmpty()) {
                            true -> {
                                error = null
                                true
                            } // End of case true
                            false -> {
                                error =
                                    getString(R.string.my_page_nickname_modify_error_dialog_inform_textview_text)
                                false
                            } // End of case false
                        } // End of when
                    if (text.toString().isEmpty())
                        result = false
                    setNextButtonAvailable(result)
                } // End of afterTextChanged
            }) // End of addTextChangedListener
        }
    }

}