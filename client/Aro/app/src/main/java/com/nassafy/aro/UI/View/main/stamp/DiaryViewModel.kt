package com.nassafy.aro.ui.view.main.stamp

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.nassafy.aro.data.dto.Diary
import com.nassafy.aro.domain.repository.DiaryRepository
import com.nassafy.aro.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.util.*
import javax.inject.Inject


private const val TAG = "DiaryViewModel_싸피"

@HiltViewModel
class DiaryViewModel @Inject constructor(private val diaryRepository: DiaryRepository) :
    ViewModel() {

    // diary viewModel Data
    private val _diaryData: Diary = Diary()
    val diaryData: Diary
        get() = _diaryData

    // 유저가 선택한 이미지
    private val _selectImageLiveData = MutableLiveData<Uri>()
    val selectImageLiveData: LiveData<Uri>
        get() = _selectImageLiveData


    // 선택한 이미지 리스트
    private val _selectImageListLiveData = MutableLiveData<LinkedList<Uri>>(LinkedList())
    val selectImageListLiveData: MutableLiveData<LinkedList<Uri>>
        get() = _selectImageListLiveData

    fun setSelectImageListAddImage(savedImageList: MutableList<Uri>) {
        savedImageList.forEach {
            selectImageListAddImage(it)
        }
    } // End of setSelectImageListAddImage

    fun selectImageListAddImage(iamgeUri: Uri) {
        selectImageListLiveData.value!!.add(iamgeUri)
    } // End of selectImageListAddImage

    fun selectImageListRemoveImage(index: Int) {
        selectImageListLiveData.value!!.removeAt(index)
    } // End of selectImageListRemoveImage

    // ===================================== ViewPager ImageList LiveData =====================================
    // 뷰페이저에 들어가는 이미지 리스트를 관리

    private val _viewPagerImageListSizeLiveData = MutableLiveData<Int>(0)
    val viewPagerImageListSizeLiveData: LiveData<Int>
        get() = _viewPagerImageListSizeLiveData

    fun viewPagerImageListSizeMinus() {
        _viewPagerImageListSizeLiveData.value = _viewPagerImageListSizeLiveData.value?.minus(1)
    } // End of viewPagerImageListSizeReduce

    fun viewPagerImageListSizePlus() {
        _viewPagerImageListSizeLiveData.value = _viewPagerImageListSizeLiveData.value?.plus(1)
    }


    // ===================================== 명소별 유저 diary 데이터 가져오기 =====================================
    val getPlaceDiaryUserDataResponseLiveData: LiveData<NetworkResult<Diary>>
        get() = diaryRepository.getPlaceDiaryUserDataResponseLiveData

    suspend fun getPlaceDiaryUserData(
        placeId: Long
    ) {
        viewModelScope.launch {
            diaryRepository.getPlaceDiaryUserData(placeId)
        }
    } // End of getPlaceDiaryUserData

    // ===================================== 명소별 다이어리 생성 =====================================
    val createPlaceDiaryResponseLiveData: LiveData<NetworkResult<Int>>
        get() = diaryRepository.createPlaceDiaryResponseLiveData

    suspend fun createPlaceDiary(
        placeId: Long,
        deleteImageList: List<String>,
        newImageList: List<MultipartBody.Part?>,
        memo: String
    ) {
        var gson = Gson()
        var gsonPretty = GsonBuilder().setPrettyPrinting().create()

        var dataJson = JsonObject().apply {
            addProperty("memo", memo)
            addProperty("deleteImageList", JSONArray(deleteImageList).toString())
        }

        val requestHashMap: HashMap<String, RequestBody> = HashMap()

        requestHashMap["memo"] = memo.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val jsonArray = JSONArray(deleteImageList)
        requestHashMap["deleteImageList"] =
            jsonArray.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        when (newImageList.isEmpty()) {
            true -> {
                viewModelScope.launch {
                    diaryRepository.createPlaceDiary(
                        placeId, null, requestHashMap
                    )
                }
            }
            false -> {
                viewModelScope.launch {
                    diaryRepository.createPlaceDiary(
                        placeId, newImageList, requestHashMap
                    )
                }
            }
        }

//        viewModelScope.launch {
//            diaryRepository.createPlaceDiary(
//                placeId, newImageList, requestHashMap
//            )
//        }
    } // End of createPlaceDiary
} // End of DiaryViewModel

