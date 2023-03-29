package com.nassafy.aro.ui.view.main.stamp

import android.app.Activity.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.Diary
import com.nassafy.aro.databinding.FragmentStampDiaryBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.util.ChangeMultipartUtil
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

private const val TAG = "StampDiaryFragment_싸피"

@AndroidEntryPoint
class StampDiaryFragment() :
    BaseFragment<FragmentStampDiaryBinding>(FragmentStampDiaryBinding::inflate) {
    private lateinit var mContext: Context

    // viewPager
    private lateinit var stampDiaryImageViewpager: ViewPager2

    // viewPageAdapter
    private lateinit var stampDiaryImageViewpagerAdapter: StampDiaryImageViewPagerAdapter

    // Navigation ViewModel
    private val stampNavViewModel: StampNavViewModel by navGraphViewModels(R.id.nav_stamp_diary)

    // Fragment ViewModel
    private val diaryViewModel: DiaryViewModel by viewModels()

    // 기존에 있던 이미지 파일
    //private var savedImageList: MutableList<String> = LinkedList()

    // 새로 추가한 이미지 파일들
    private var newImageList: MutableList<MultipartBody.Part?> = LinkedList()
    private var newImageStringList: MutableList<Uri> = LinkedList()

    // 삭제할 이미지
    private var deleteImageList: MutableList<String> = LinkedList()

    // 어댑터에 들어가는 전체 이미지 리스트
    private var viewPagerAdapterWholeImageList: MutableList<Uri> = LinkedList()

    // userDiaryData
    private lateinit var userDiaryData: Diary

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    } // End of onAttach

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // observers
        getPlaceDiaryUserDataResponseLiveDataObserve()
        selectImageLiveDataObserve()
        createPlaceDiaryResponseLiveDataObserve()
        viewPagerImageListObserve()

        // getData
        CoroutineScope(Dispatchers.IO).launch {
            diaryViewModel.getPlaceDiaryUserData(stampNavViewModel.selectedPlaceLiveData.value!!.attractionId!!)

            withContext(Dispatchers.Main) {
                initViewPagerAdapter()
            }
        }

        initEventListeners()
    } // End of onViewCreated

    // 뷰 페이저에 들어가는 이미지 리스트 상태값을 위해서 LiveData를 사용
    private fun initViewPagerAdapter() {
        stampDiaryImageViewpager = binding.stampDiaryImageViewpager2
        stampDiaryImageViewpagerAdapter =
            StampDiaryImageViewPagerAdapter(viewPagerAdapterWholeImageList as LinkedList<Uri>)
        diaryViewModel.setSelectImageListAddImage(viewPagerAdapterWholeImageList)

        stampDiaryImageViewpager.apply {
            adapter = stampDiaryImageViewpagerAdapter
            ViewPager2.ORIENTATION_HORIZONTAL
        }

        stampDiaryImageViewpagerAdapter.setItemClickListener(object :
            StampDiaryImageViewPagerAdapter.ItemClickListener {
            // 이미지 삭제하기 버튼 눌렀을 때 이벤트 처리
            override fun imageRemoveButtonClick(position: Int) {
                // position 번째의 이미지를 삭제함
                // 새로운 이미지를 삭제할 때,
                if (newImageStringList.contains(viewPagerAdapterWholeImageList[position])) {
                    val index = newImageStringList.indexOf(viewPagerAdapterWholeImageList[position])
                    newImageList.removeAt(index)
                    newImageStringList.removeAt(index)
                    viewPagerAdapterWholeImageList.removeAt(position)
                    diaryViewModel.viewPagerImageListSizeMinus()
                } else {
                    deleteImageList.add(viewPagerAdapterWholeImageList[position].toString())
                    viewPagerAdapterWholeImageList.removeAt(position)
                }

                stampDiaryImageViewpagerAdapter.refreshAdapter()
            }
        })

        stampDiaryImageViewpagerAdapter.refreshAdapter()
        stampDiaryImageViewpagerAdapter.notifyDataSetChanged()

        TabLayoutMediator(
            binding.stampDiaryImageIndicator, binding.stampDiaryImageViewpager2
        ) { tab, position ->
//            if (tab.view.isSelected == true) {
//                tab.view.background =
//                    resources.getDrawable(R.drawable.diary_image_pager_indicator_icon_isselected_true)
//            }
//
//            if (tab.view.isSelected == false) {
//                tab.view.background =
//                    resources.getDrawable(R.drawable.diary_image_pager_indicator_icon_isselected_false)
//            }
            //tab.text = countryPlaceList[position].countryName
            //tab.icon = resources.getDrawable(R.drawable.diary_image_pager_indicator_icon_isselected_false)
        }.attach()
    } // End of initAdapter


    private fun selectImageLiveDataObserve() {
        diaryViewModel.selectImageLiveData.observe(this.viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {
                diaryViewModel.selectImageListAddImage(it)
                stampDiaryImageViewpagerAdapter.addImage(it)
            }
        }
    } // End of selectImageLiveDataObserve

    private fun selectGallery() {
//        val writePermission = ContextCompat.checkSelfPermission(
//            mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        val readPermission = ContextCompat.checkSelfPermission(
//            mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//
//        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(
//                mContext as Activity, arrayOf(
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                ), REQ_GALLERY
//            )
//        } else {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.setDataAndType(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
//            )
//            imageResult.launch(intent)
//        }

        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
        )
        imageResult.launch(intent)
    } // End of selectGallery

    private fun initViewGetData() {
        // 현재 선택되어있는 명소와 국가 데이터를 가져옴
        val selectedPlace = stampNavViewModel.selectedPlaceLiveData.value
        val selectedCountry = stampNavViewModel.selectedCountry

        binding.stampDiaryCountryNameTextview.text = selectedCountry
        binding.stampDiaryCountryPlaceNameTextview.text = selectedPlace!!.attractionName.toString()
        binding.stampDiaryCountryPlaceInformTextview.text = selectedPlace.description.toString()
        binding.stampDiaryHistoryEdittext.setText(userDiaryData.memo)
    } // End of initViewGetData

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 가져온 이미지가 있을 경우 해당 데이터를 불러옴.
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult
            stampDiaryImageViewpagerAdapter.addImage(imageUri)
            newImageStringList.add(imageUri)
            diaryViewModel.viewPagerImageListSizePlus()

            val file = File(
                ChangeMultipartUtil().changeAbsoluteyPath(imageUri, requireActivity())
            )

            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("newImageList", file.name, requestFile)
            newImageList.add(body)
        }
    } // End of registerForActivityResult


    private fun getPlaceDiaryUserDataResponseLiveDataObserve() {
        diaryViewModel.getPlaceDiaryUserDataResponseLiveData.observe(this.viewLifecycleOwner) {
            userDiaryData = it.data ?: Diary()

            if (userDiaryData.images.isNotEmpty()) {
                userDiaryData.images.forEach {
                    viewPagerAdapterWholeImageList.add(it.toUri())
                    diaryViewModel.viewPagerImageListSizePlus()
                }
            }

            initViewGetData()
            initViewPagerAdapter()
        }
    } // End of getPlaceDiaryUserDataResponseLiveDataObserve

    private fun initEventListeners() {
        // 이미지 추가 버튼 클릭.
        binding.stampDiaryHistoryImageAddButton.setOnClickListener {
            selectGallery()
        }

        // 저장 버튼 클릭
        binding.stampDiarySaveButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    binding.stampDiaryProgressbar.visibility = View.VISIBLE
                    binding.stampDiaryProgressbar.isVisible = true
                    binding.stampDiaryProgressbarInformText.visibility = View.VISIBLE
                    binding.stampDiaryProgressbarInformText.isVisible = true
                    binding.stampDiaryNestedscrollview.visibility = View.INVISIBLE
                    binding.stampDiaryNestedscrollview.isVisible = false
                }

                diaryViewModel.createPlaceDiary(
                    stampNavViewModel.selectedPlaceLiveData.value!!.attractionId!!,
                    deleteImageList,
                    newImageList,
                    binding.stampDiaryHistoryEdittext.text.toString()
                )
            }
        }
    } // End of eventListeners

    private fun createPlaceDiaryResponseLiveDataObserve() {
        diaryViewModel.createPlaceDiaryResponseLiveData.observe(this.viewLifecycleOwner) {
            binding.stampDiaryProgressbar.visibility = View.GONE
            binding.stampDiaryProgressbar.isVisible = false
            binding.stampDiaryProgressbarInformText.visibility = View.GONE
            binding.stampDiaryProgressbarInformText.isVisible = false

            val layout = layoutInflater.inflate(R.layout.custom_toast, null)

            Toast(mContext).apply {
                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.CENTER, 0, 0)
                view = layout
            }.show()

            when (it) {
                is NetworkResult.Success -> {
                    if (it.data == 200) {
                        findNavController().popBackStack()
                    }
                }

                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("저장 실패!")
                }

                is NetworkResult.Loading -> {
                    binding.stampDiaryProgressbar.visibility = View.VISIBLE
                    binding.stampDiaryProgressbar.isVisible = true
                    binding.stampDiaryProgressbarInformText.visibility = View.VISIBLE
                    binding.stampDiaryProgressbarInformText.isVisible = true
                }
            }
        }
    } // End of createPlaceDiaryResponseLiveDataObserve

    private fun viewPagerImageListObserve() {
        diaryViewModel.viewPagerImageListSizeLiveData.observe(this.viewLifecycleOwner) {

            if (it >= 5) {
                binding.stampDiaryHistoryImageAddButton.visibility = View.GONE
                binding.stampDiaryHistoryImageAddButton.isVisible = false
            } else if (it < 5) {
                binding.stampDiaryHistoryImageAddButton.visibility = View.VISIBLE
                binding.stampDiaryHistoryImageAddButton.isVisible = true
            }

            if (it == 0) {
                binding.stampDiaryImagePagerInformTextTextview.visibility = View.VISIBLE
                binding.stampDiaryImagePagerInformTextTextview.isVisible = true
                binding.stampDiaryImagePagerSubInformTextTextview.visibility = View.VISIBLE
                binding.stampDiaryImagePagerSubInformTextTextview.isVisible = true
            } else if (it >= 1) {
                binding.stampDiaryImagePagerInformTextTextview.visibility = View.GONE
                binding.stampDiaryImagePagerInformTextTextview.isVisible = false
                binding.stampDiaryImagePagerSubInformTextTextview.visibility = View.GONE
                binding.stampDiaryImagePagerSubInformTextTextview.isVisible = false
            }
        }
    } // End of viewPagerImageListObserve

    companion object {
        const val REQ_GALLERY = 1
    }
} // End of StampDiaryFragment
