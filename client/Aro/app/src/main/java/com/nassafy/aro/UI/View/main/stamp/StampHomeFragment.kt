package com.nassafy.aro.ui.view.main.stamp

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.nassafy.aro.R
import com.nassafy.aro.databinding.FragmentStampHomeBinding
import com.nassafy.aro.ui.adapter.CountrySpinnerAdapter
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


private const val TAG = "StampHomeFragment_싸피"

@AndroidEntryPoint
class StampHomeFragment :
    BaseFragment<FragmentStampHomeBinding>(FragmentStampHomeBinding::inflate) {
    private lateinit var mContext: Context

    // viewModel
    private val stampHomeViewModel: StampHomeViewModel by viewModels()
    private val stampHomeNavViewModel: StampNavViewModel by navGraphViewModels(R.id.nav_stamp_diary)

    private var countryList: List<String> = ArrayList()

    // ArrayAdapter
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    } // End of onAttach

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val main = requireActivity() as MainActivity
        main.closeDrawer()
    } // End of onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰모델 라이브 데이터 옵저버들 등록.
        getAllNationListResponseLiveDataObserve()
        getUserStampDataGroupByCountryResponseLiveDataObserve()

        // 처음 데이터 가져오기.
        initViewgetData()

        // 이벤트 리스너들 등록
        initEventListeners()

        var moveX = 0f
        var moveY = 0f
        binding.stampHomeImageview.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveX = view.x - motionEvent.rawX
                    moveY = view.y - motionEvent.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    view.animate().x(motionEvent.rawX + moveX).y(motionEvent.rawY + moveY)
                        .setDuration(0).start()
                }
            }

            true
        }
    } // End of onViewCreated

    private fun initEventListeners() {
        // 뒤로가기 버튼 클릭 이벤트
        binding.stampHomeBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // 국가별 명소 상세보기 버튼 클릭 이벤트
        binding.stampHomeDetailButtonTextview.setOnClickListener {
            // 번들로 국가 넘겨줌.
            //val bundle = bundleOf("selectedCountryName" to selectedCountry.toString())

            Navigation.findNavController(binding.stampHomeDetailButtonTextview)
                .navigate(R.id.action_stampHomeFragment_to_stampCountryPlacesFragment)
        }
    } // End of initEventListeners

    private fun spinnerEventListener() {
        binding.stampHomeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val countryName = countryList[position]
                    stampHomeNavViewModel.setSelectedCountry(countryName)

                    CoroutineScope(Dispatchers.IO).launch {
                        stampHomeViewModel.getUserStampDataGroupByCountry(countryName)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    return
                }
            }
    } // End of spinnerEventListener

    private fun initSpinner(countryList: List<String>) {
        arrayAdapter = CountrySpinnerAdapter(mContext, R.layout.item_country_spinner, countryList)
        binding.stampHomeSpinner.adapter = arrayAdapter
    } // End of initSpinner

    private fun initViewgetData() {
        if (stampHomeNavViewModel.countryList.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                stampHomeViewModel.getAllNationList()
            }
        }
    } // End of initViewgetData

    private fun getUserStampDataGroupByCountryResponseLiveDataObserve() {
        stampHomeViewModel.getUserStampDataGroupByCountryResponseLiveData.observe(this.viewLifecycleOwner) {
            binding.stampHomeProgressbar.visibility = View.GONE
            binding.stampHomeProgressbar.isVisible = false
            binding.stampHomeConstlayout.visibility = View.VISIBLE
            binding.stampHomeConstlayout.isVisible = true


            when (it) {
                is NetworkResult.Success -> {
                    Picasso.get().load(it.data!!.mapImage).fit().centerCrop()
                        .into(binding.stampHomeImageview)
                }

                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }

                is NetworkResult.Loading -> {
                    binding.stampHomeProgressbar.visibility = View.VISIBLE
                    binding.stampHomeProgressbar.isVisible = true
                    binding.stampHomeConstlayout.visibility = View.INVISIBLE
                    binding.stampHomeConstlayout.isVisible = false
                }
            }
        }
    } // End of getUserStampDataGroupByCountryResponseLiveDataObserve

    private fun getAllNationListResponseLiveDataObserve() {
        stampHomeViewModel.getAllNationListResponseLiveData.observe(this.viewLifecycleOwner) {
            binding.stampHomeProgressbar.visibility = View.GONE
            binding.stampHomeProgressbar.isVisible = false
            binding.stampHomeConstlayout.visibility = View.VISIBLE
            binding.stampHomeConstlayout.isVisible = true

            when (it) {
                is NetworkResult.Success -> {
                    countryList = it.data as ArrayList<String>
                    stampHomeNavViewModel.setCountryList(countryList)

                    CoroutineScope(Dispatchers.Main).launch {
                        val spinnerDef: Deferred<Int> = async {
                            initSpinner(countryList)
                            1
                        }


                        spinnerDef.await()

                        withContext(Dispatchers.Default) {
                            spinnerEventListener()
                        }
                    }


                }

                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }

                is NetworkResult.Loading -> {
                    binding.stampHomeProgressbar.visibility = View.VISIBLE
                    binding.stampHomeProgressbar.isVisible = true
                    binding.stampHomeConstlayout.visibility = View.INVISIBLE
                    binding.stampHomeConstlayout.isVisible = false
                }
            }
        }
    } // End of getAllNationListResponseLiveDataObserve
} // End of StampHomeFragment class
