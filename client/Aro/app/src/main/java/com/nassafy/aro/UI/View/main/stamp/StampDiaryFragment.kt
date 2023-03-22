package com.nassafy.aro.ui.view.main.stamp

import android.app.Activity.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nassafy.aro.data.dto.PlaceDiaryTest
import com.nassafy.aro.databinding.FragmentStampDiaryBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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


    // viewModel
    private val diaryViewModel: DiaryViewModel by viewModels()

    private lateinit var diaryImageList: LinkedList<Uri>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    } // End of onAttach

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diaryImageList = LinkedList()
    } // End of onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectImageLiveDataObserve()

        binding.stampDiaryHistoryImageAddButton.setOnClickListener {
            selectGallery()
        }

        // 번들 가져오기
        val placeDiaryTest = requireArguments().getParcelable<PlaceDiaryTest>("placeDiaryTest")

        if (diaryViewModel.selectImageListLiveData.value != null) {
            diaryImageList.addAll(diaryViewModel.selectImageListLiveData.value!!)
        } else {
            diaryViewModel.selectImageListLiveData.value = LinkedList()
            diaryImageList = LinkedList()
        }

        initViewPagerAdapter()


//        when (diaryImageList.isEmpty()) {
//            true -> {
//                binding.stampDiaryImagePagerInformTextTextview.visibility = View.VISIBLE
//                binding.stampDiaryCountryPlaceInformTextview.visibility = View.VISIBLE
//            }
//            false -> {
//                initViewPagerAdapter()
//                selectImageLiveDataObserve()
//                binding.stampDiaryImagePagerInformTextTextview.visibility = View.GONE
//                binding.stampDiaryCountryPlaceInformTextview.visibility = View.GONE
//            }
//        }

        initEventListeners()


        binding.stampDiaryCountryNameTextview.text = placeDiaryTest!!.countryName.toString()
        binding.stampDiaryCountryPlaceNameTextview.text = placeDiaryTest!!.placeName.toString()
        binding.stampDiaryCountryPlaceInformTextview.text =
            placeDiaryTest.placeExplanation.toString()
        binding.stampDiaryHistoryEdittext.setText(placeDiaryTest.diaryContent.toString())

        // 이미지가 5개가 될 경우 더이상 추가할 수 없으므로 버튼을 보이지 않도록 함
        when (diaryImageList.size) {
            5 -> binding.stampDiaryHistoryImageAddButton.visibility = View.GONE
            else -> binding.stampDiaryHistoryImageAddButton.visibility = View.VISIBLE
        }
    } // End of onViewCreated

    private fun initViewPagerAdapter() {
        stampDiaryImageViewpager = binding.stampDiaryImageViewpager2
        stampDiaryImageViewpagerAdapter = StampDiaryImageViewPagerAdapter(diaryImageList)

        stampDiaryImageViewpager.apply {
            adapter = stampDiaryImageViewpagerAdapter
            ViewPager2.ORIENTATION_HORIZONTAL
        }

        stampDiaryImageViewpagerAdapter.setItemClickListener(object :
            StampDiaryImageViewPagerAdapter.ItemClickListener {
            override fun imageRemoveButtonClick(position: Int) {

                // position 번째의 이미지를 삭제함
                diaryViewModel.selectImageListRemoveImage(position)
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

    private fun initEventListeners() {
        // 이미지 추가 버튼 클릭.


        // 저장 버튼 클릭
        binding.stampDiarySaveButton.setOnClickListener {
            requireView().showSnackBarMessage("저장 버튼 클릭됨")

            CoroutineScope(Dispatchers.IO).launch {
                diaryViewModel.createPlaceDiary(
                    "미국", "페어뱅크스", 3L
                )
            }
        }

    } // End of eventListeners

    private fun selectImageLiveDataObserve() {
        diaryViewModel.selectImageListLiveData.observe(this.viewLifecycleOwner) {
            Log.d(TAG, "selectImageLiveDataObserve: 옵저버 동작함?")

            stampDiaryImageViewpagerAdapter.refreshAdapter()
            stampDiaryImageViewpagerAdapter.notifyDataSetChanged()
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

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 가져온 이미지가 있을 경우 해당 데이터를 불러옴.
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult
            Log.d(TAG, "이미지는 가져와 지냐?: ${imageUri}")
            diaryImageList.add(imageUri)
            diaryViewModel.selectImageListLiveData.postValue(diaryImageList)
        }
    } // End of registerForActivityResult

    companion object {
        const val REQ_GALLERY = 1
    }
} // End of StampDiaryFragment