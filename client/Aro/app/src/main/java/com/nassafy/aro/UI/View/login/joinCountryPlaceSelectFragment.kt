package com.nassafy.aro.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.nassafy.aro.Application
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.UserTest
import com.nassafy.aro.databinding.FragmentAroCountryPlaceSelectBinding
import com.nassafy.aro.ui.adapter.CountrySpinnerAdapter
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.custom.CountryPlaceChips
import com.nassafy.aro.ui.view.custom.CountryPlaceLazyColumn
import com.nassafy.aro.ui.view.login.viewmodel.JoinCountryPlaceSelectFragmentViewModel
import com.nassafy.aro.ui.view.login.viewmodel.LoginActivityViewModel
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.json.JSONArray

@AndroidEntryPoint
class JoinCountryPlaceSelectFragment : BaseFragment<FragmentAroCountryPlaceSelectBinding>(
    FragmentAroCountryPlaceSelectBinding::inflate
) {


    private val loginActivityViewModel: LoginActivityViewModel by activityViewModels()
    private val joinCountryPlaceServiceSelectFragmentViewModel: JoinCountryPlaceSelectFragmentViewModel by viewModels()
    private var spinnerList = arrayListOf<String>()
    private lateinit var adapter: CountrySpinnerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.sign_up_button
            )
        )
        CoroutineScope(Dispatchers.IO).launch {
            val result: Deferred<Int> = async {
                loginActivityViewModel.getCountryList()
                0
            }
            result.await()
        }

        initObserve()
        initView()
    }

    private fun initView() {
        initSpinner(spinnerList)
        binding.nextButton.setOnClickListener {

            loginActivityViewModel.apply {
                val gson = GsonBuilder().create()
                val user = JsonObject().apply {
                    addProperty("providerType", providerType)
                    addProperty("email", email)
                    addProperty("password", password)
                    addProperty("nickname", nickname)
                    addProperty("auroraService", loginActivityViewModel.isAuroraServiceSelected)
                    addProperty("meteorService", loginActivityViewModel.isMeteorServiceSelected)
                }
                user.add(
                    "auroraPlaces",
                    gson.toJsonTree(selectedAuroraPlaceList.map { it.placeId })
                        .getAsJsonArray()
                )
                user.add(
                    "meteorPlaces",
                    gson.toJsonTree(selectedMeteorPlaces.value?.map { it.placeId } ?: emptyList<PlaceItem>())
                        .getAsJsonArray()
                )
                joinCountryPlaceServiceSelectFragmentViewModel.join(user)
            }
        }
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        initComposeView()
    }

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
                        delay(200)
                        binding.loadingLayout.isGone = true
                    }
                    loginActivityViewModel.getPlaceList(
                        parent?.getItemAtPosition(position).toString()
                    )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }// End of onItemSelectedListener
    } // End of initSpinner

    private fun initObserve() {
        loginActivityViewModel.countryListLiveData.observe(this.viewLifecycleOwner) {
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


        loginActivityViewModel.placeListLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    loginActivityViewModel.placeListData.clear()
                    loginActivityViewModel.placeListData.addAll(it.data!!)
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }
                is NetworkResult.Loading -> {
                    Log.d(
                        "ssafy_pcs", "로딩 중.."
                    )
                }
            } // End of when
        } // End of .placeListLiveData.observe(this.viewLifecycleOwner)

        loginActivityViewModel.selectedAuroraPlaces.observe(this.viewLifecycleOwner) { selectedAuroraPlaces ->
            loginActivityViewModel.selectedAuroraPlaceList.clear()
            loginActivityViewModel.selectedAuroraPlaceList.addAll(selectedAuroraPlaces)
        }

        joinCountryPlaceServiceSelectFragmentViewModel.userJoinNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    when (loginActivityViewModel.placeListLiveData.value!!) {
                        is NetworkResult.Success<List<PlaceItem>> -> {
                            requireView().showSnackBarMessage("회원가입 성공!")
                            Log.d("ssafy_pcs/it", it.toString())
                            Log.d("ssafy_pcs/it.data", it.data.toString())
                            Application.sharedPreferencesUtil.addUserAccessToken(
                                it.data?.accessToken ?: ""
                            )
                            Application.sharedPreferencesUtil.addUserRefreshToken(
                                it.data?.refreshToken ?: ""
                            )
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        is NetworkResult.Error<*> -> {
                            requireView().showSnackBarMessage("서버 통신 에러 발생")
                        }
                        is NetworkResult.Loading<*> -> {
                            Log.d("ssafy_pcs", "로딩 중..")
                        }
                    }
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }
                is NetworkResult.Loading -> {
                    Log.d("ssafy_pcs", "로딩 중..")
                }
            }
        } // End of userJoinNetworkResultLiveData.observe

    }


    private fun initComposeView() {
        binding.countryPlaceComposeview.apply {
            // Dispose of the Composition when the view's LifecycleOwner

            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val placeList = remember { loginActivityViewModel.placeListData }
                val selectedAuroraPlaceList =
                    remember { loginActivityViewModel.selectedAuroraPlaceList }
                // In Compose world
                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .height(this.height.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CountryPlaceChips(selectedAuroraPlaceList, loginActivityViewModel)
                        Divider(
                            modifier = Modifier
                                .height(2.dp)
                                .padding(horizontal = 30.dp),
                            color = Color.White
                        ) // End of Divider
                        CountryPlaceLazyColumn(
                            placeList,
                            selectedAuroraPlaceList,
                            loginActivityViewModel
                        )
                    } // End of Column
                }
            }
        }
        binding.cancelButton.setOnClickListener {
            loginActivityViewModel.isAuroraServiceSelected = false
            loginActivityViewModel.isMeteorServiceSelected = false
            loginActivityViewModel.clearSelectedAuroraPlaceList()
            findNavController().popBackStack()
        }
    } // End of initView
}