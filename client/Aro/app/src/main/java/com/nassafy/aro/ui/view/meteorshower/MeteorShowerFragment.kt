package com.nassafy.aro.ui.view.meteorshower

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.nassafy.aro.databinding.FragmentMeteorShowerBinding
import com.nassafy.aro.ui.adapter.MeteorShowerAdapter
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.ui.view.main.MainActivityViewModel
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MeteorShowerFragment_싸피"

@AndroidEntryPoint
class MeteorShowerFragment :
    BaseFragment<FragmentMeteorShowerBinding>(FragmentMeteorShowerBinding::inflate) {
    private lateinit var meteorShowerAdapter: MeteorShowerAdapter
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var mLayoutManager: LinearLayoutManager

    // Context
    private lateinit var mContext: Context

    // Fragment ViewModel
    private val meteorShowerViewModel: MeteorShowerViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    } // End of onAttach

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observe
        getMyMeteorShowerResponseLiveDataObserve()

        if (mainActivityViewModel.meteorShowerServiceEnabled) {
            mLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            CoroutineScope(Dispatchers.IO).launch {
                meteorShowerViewModel.getMyMeteorShower()
            }

            binding.meteorShowerCountryTextview.apply {
                text =
                    meteorShowerViewModel.getMyMeteorShowerResponseLiveData.value!!.data!!.nation.toString()
//                setOnClickListener {
//                    val meteorCountrySelectDialog = MeteorCountrySelectDialog(
//                        countryList
//                    )
//                    meteorCountrySelectDialog.show(
//                        childFragmentManager, "MeteorCountrySelectDialog"
//                    )
//                }
            }
            binding.drawerImagebutton.setOnClickListener {
                val mainActivity = activity as MainActivity
                mainActivity.openDrawer()
            }

            binding.meteorShowerCountryTextview.visibility = View.VISIBLE
            binding.meteorShowerRecyclerview.visibility = View.VISIBLE
            binding.altView.root.visibility = View.GONE
        } else {
            binding.meteorShowerCountryTextview.visibility = View.GONE
            binding.meteorShowerRecyclerview.visibility = View.GONE
            binding.drawerImagebutton.setOnClickListener {
                val mainActivity = activity as MainActivity
                mainActivity.openDrawer()
            }
        }

    } // End of onViewCreated

    private fun setAdapter() {
        meteorShowerAdapter = MeteorShowerAdapter(
            mContext,
            meteorShowerViewModel.getMyMeteorShowerResponseLiveData.value!!.data!!.meteorList!!
        )

        binding.meteorShowerRecyclerview.apply {
            adapter = meteorShowerAdapter
            layoutManager = mLayoutManager
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }


        binding.meteorShowerRecyclerview.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            //binding.meteorShowerHeaderFramelayout.alpha = getAlpahForFloatingButton(scrollY)

            if (scrollY > oldScrollY + 2) {
                binding.meteorShowerHeaderFramelayout.visibility = View.GONE
            }

            // 스크롤 위로
            if (scrollY < oldScrollY - 2) {
                binding.meteorShowerHeaderFramelayout.visibility = View.VISIBLE
            }
        }
    } // End of setAdapter

    private fun getAlpahForFloatingButton(scrollY: Int): Float {
        val minDist = 0
        val maxDist = 10000
        if (scrollY > maxDist / 100) {
            return 1.0f
        } else if (scrollY < minDist) {
            return 0f
        } else {
            var alpha = 0f
            alpha = ((255.0 / maxDist) * scrollY).toFloat()
            return alpha
        }
    }

    private fun getMyMeteorShowerResponseLiveDataObserve() {
        meteorShowerViewModel.getMyMeteorShowerResponseLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {

                    setAdapter()
                }

                is NetworkResult.Error -> {
                    Log.d(
                        TAG, "getUserPlaceDataGroupByCountryResponseLiveDataObserve: ${it.message}"
                    )
                }

                is NetworkResult.Loading -> {
                    Log.d(
                        TAG, "getUserPlaceDataGroupByCountryResponseLiveDataObserve: 로딩 중"
                    )
                }
            }
        }
    } // End of getMyMeteorShowerResponseLiveDataObserve
}
