package com.nassafy.aro.ui.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.nassafy.aro.databinding.FragmentServerCallTestBinding
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "serverCallTestFragment_싸피"

@AndroidEntryPoint
class ServerCallTestFragment :
    BaseFragment<FragmentServerCallTestBinding>(FragmentServerCallTestBinding::inflate) {

    // context
    private lateinit var mContext: Context

    // viewModel
    private val serverCallTestViewModel: ServerCallTestViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    } // End of onAttach

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    } // End of onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()

        binding.serverCallTestButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                serverCallTestViewModel.getServerCallTest()
            }
        }
    } // End of onViewCreated

    private fun initObserve() {
        getServerCallTestObserve()
    } // End of initObserve

    private fun getServerCallTestObserve() {
        serverCallTestViewModel.getServerCallTestResponseLiveData.observe(this.viewLifecycleOwner) {
            binding.serverCallTestProgressbar.visibility = View.GONE
            binding.serverCallTestProgressbar.isVisible = false

            when (it) {
                is NetworkResult.Success -> {
                    binding.serverCallResultTextview.text = it.data.toString()
                    Log.d(TAG, "getServerCallTestObserve: ${it.data}")
                }

                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                    Log.e(TAG, "getServerCallTestObserve: 서버 통신 에러 발생")
                }

                is NetworkResult.Loading -> {
                    Log.d(TAG, "getServerCallTestObserve: 로딩중")
                    binding.serverCallTestProgressbar.visibility = View.VISIBLE
                    binding.serverCallTestProgressbar.isVisible = true
                }
            }
        }
    } // End of getServerCallTestObserve
} // End of serverCallTestFragment class
