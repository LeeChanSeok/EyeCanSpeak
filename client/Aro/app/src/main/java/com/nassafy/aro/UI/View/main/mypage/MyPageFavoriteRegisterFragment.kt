package com.nassafy.aro.ui.view.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.*
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.databinding.FragmentAroCountryPlaceSelectBinding
import com.nassafy.aro.ui.adapter.CountrySpinnerAdapter
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.custom.CountryPlaceChips
import com.nassafy.aro.ui.view.custom.CountryPlaceLazyColumn
import com.nassafy.aro.ui.view.main.mypage.viewmodel.MyPageFavoriteRegisterFragmentViewModel
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MyPageFavoriteRegisterFragment :
    BaseFragment<FragmentAroCountryPlaceSelectBinding>(FragmentAroCountryPlaceSelectBinding::inflate) {

    lateinit var adapter: CountrySpinnerAdapter
    private var spinnerList = arrayListOf<String>()
    private val myPageFavoriteRegisterFragmentViewModel: MyPageFavoriteRegisterFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MyPageFavoriteRegisterFragmentArgs by navArgs()
        myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected = args.auroraService
        myPageFavoriteRegisterFragmentViewModel.isMeteorServiceSelected = args.meteorService

        initObserve()
        CoroutineScope(Dispatchers.IO).launch {
            myPageFavoriteRegisterFragmentViewModel.getCountryList()
        } // End of CoroutineScope
        initView()
    } // End of onVIewCreated

    private fun initObserve() {
        myPageFavoriteRegisterFragmentViewModel.countryListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(800)
                        binding.loadingLayout.isGone = true
                    }
                    spinnerList.clear()
                    spinnerList.addAll(it.data!!)
                    adapter.notifyDataSetChanged()
                    binding.selectCountryPlaceSpinner.adapter = adapter
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }
                is NetworkResult.Loading -> {
                    binding.loadingLayout.isVisible = true
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                }
            }
        } // End of countryListLiveData.observe

        myPageFavoriteRegisterFragmentViewModel.favoriteListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFavoriteRegisterFragmentViewModel.favoriteAuroraPlaceList.clear()
                    myPageFavoriteRegisterFragmentViewModel.favoriteAuroraPlaceList.addAll(
                        it.data?.attractionInterestOrNotDTOList ?: emptyList()
                    ) // myPageFavoriteRegisterFragmentViewModel.favoriteAuroraPlaceList.addAll(it.data?.memteorInterestOrNotDTO!!)
                }// End of NetworkResult.Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("닉네임 재설정에 실패했습니다.")
                } // End of NetworkResult.Error
                is NetworkResult.Loading -> {

                } // End of NetworkResult.Loading
            } // End of when
        } // End of favoriteListNetworkResultLiveData.observe

        myPageFavoriteRegisterFragmentViewModel.placeListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFavoriteRegisterFragmentViewModel.placeList.clear()
                    myPageFavoriteRegisterFragmentViewModel.placeList.addAll(it.data!!)
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }
                is NetworkResult.Loading -> {
                    //TODO Loading
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                }
            } // End of when
        } // End of .placeListLiveData.observe(this.viewLifecycleOwner)

        myPageFavoriteRegisterFragmentViewModel.setSelectServiceNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val gson = GsonBuilder().create()
                    val requestBody = JsonObject()
                    CoroutineScope(Dispatchers.IO).launch {
                        myPageFavoriteRegisterFragmentViewModel.apply {
                            requestBody.add(
                                "attractionIds",
                                gson.toJsonTree(favoriteAuroraPlaceList.map { it.placeId })
                                    .getAsJsonArray()
                            )
                            postFavoriteList(requestBody)
                        } // End of viewModel.apply
                    } // End of CoroutineScope
                } // End of Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                } // End of Error
                is NetworkResult.Loading -> {
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                } // End of Loading
            } // End of when
        } // End of setSelectServiceNetworkResultLiveData.observe

        myPageFavoriteRegisterFragmentViewModel.postFavoriteListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    requireView().showSnackBarMessage(getString(R.string.my_page_my_favorite_modify_success_text))
                    when(myPageFavoriteRegisterFragmentViewModel.isMeteorServiceSelected) {
                        true -> {
                            requireView().showSnackBarMessage("유성우 서비스는 준비 중입니다.!!")
                            findNavController()
                                .navigate(R.id.action_myPageFavoriteRegisterFragment_to_myPageFragment)
                        }
                        false -> {
                            findNavController().navigate(R.id.action_myPageFavoriteRegisterFragment_to_myPageFragment)
                        }
                    }
                } // End of Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                } // End of Error
                is NetworkResult.Loading -> {
                    Log.d("ssafy_pcs", "로딩 중..")
                } // End of Loading
            } // End of when
        } // End of postFavoriteListNetworkResultLiveData.observe
    } // End of initObserve


    private fun initView() {
        initSpinner(spinnerList)
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        } // End of cancelButton.setOnClickListener
        binding.nextButton.apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.complete_button
                )
            ) // End of setImageDrawable
            setOnClickListener {
                myPageFavoriteRegisterFragmentViewModel.apply {

                    // TODO ACTIVE
                    CoroutineScope(Dispatchers.IO).launch {
                        myPageFavoriteRegisterFragmentViewModel.selectService(
                            myPageFavoriteRegisterFragmentViewModel.isAuroraServiceSelected,
                            myPageFavoriteRegisterFragmentViewModel.isMeteorServiceSelected
                        ) // End of selectService
                    } // End of CoroutineScope
                } // End of myPageFavoriteRegisterFragmentViewModel.apply
            } // End of nextButton.setOnClickListener
            initComposeView()
        } // End of nextButton.apply
    } // End of InitView

    fun initSpinner(countryList: ArrayList<String>) {
        adapter =
            CountrySpinnerAdapter(requireContext(), R.layout.item_country_spinner, countryList)
        binding.selectCountryPlaceSpinner.adapter = adapter
        binding.selectCountryPlaceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.loadingLayout.isVisible = true
                    CoroutineScope(Dispatchers.Main).launch {
                        launch(Dispatchers.IO) {
                            myPageFavoriteRegisterFragmentViewModel.getPlaceList(
                                parent?.getItemAtPosition(position).toString()
                            )
                        }
                        delay(200)
                        binding.loadingLayout.isGone = true
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }// End of onItemSelectedListener
    } // End of initSpinner

    private fun initComposeView() {
        CoroutineScope(Dispatchers.IO).launch {
            myPageFavoriteRegisterFragmentViewModel.getFavoriteList()
        }
        binding.countryPlaceComposeview.apply {
            // Dispose of the Composition when the view's LifecycleOwner

            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val placeList = remember { myPageFavoriteRegisterFragmentViewModel.placeList }
                val selectedAuroraPlaceList =
                    remember { myPageFavoriteRegisterFragmentViewModel.favoriteAuroraPlaceList }

                // In Compose world
                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .height(this.height.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CountryPlaceChips(
                            selectedAuroraPlaceList,
                            myPageFavoriteRegisterFragmentViewModel
                        )
                        Divider(
                            modifier = Modifier
                                .height(2.dp)
                                .padding(horizontal = 24.dp),
                            color = Color.White
                        ) // End of Divider
                        CountryPlaceLazyColumn(
                            placeList,
                            selectedAuroraPlaceList,
                            myPageFavoriteRegisterFragmentViewModel
                        )
                    } // End of Column
                } // End of MaterialTheme
            } // End of setContent
        } // End of countryPlaceComposeview.apply
    } // End of initComposeView
} // End of MyPageFavoriteRegisterFragment
