package com.nassafy.aro.ui.view.main.stamp

import android.app.Activity
import android.app.Activity.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.viewModels
import com.nassafy.aro.databinding.FragmentStampValidateBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.ChangeMultipartUtil
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.*

private const val TAG = "StampValidateFragment_싸피"

@AndroidEntryPoint
class StampValidateFragment :
    BaseFragment<FragmentStampValidateBinding>(FragmentStampValidateBinding::inflate) {
    private lateinit var mContext: Context

    // ViewModel
    private val validateViewModel: ValidateViewModel by viewModels()

    // 권한 체크
    private var isReadPermissionGranted = false

    // 갤러리 권한 목록
    var REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.READ_MEDIA_IMAGES,
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    } // End of onAttach

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postImageValidateResponseLiveDataObserve()

        // 사진 찍어서 가져오기
        binding.stampValidateCameraButton.setOnClickListener {
            if (isCameraServiceAvaliable()) {
                openCam()
            }
        }

        // 갤러리에서 가져온 사진.
        binding.stampValidateGalleryButton.setOnClickListener {
            isGalleryServiceAvaliable()
        }
    } // End of onViewCreated

    var image_uri: Uri? = null
    val IMAGE_CAPTURE_CODE = 654

    // 카메라 여는 메소드
    private fun openCam() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")

        image_uri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    } // End of openCam

    // ==================================== 권환 확인 ==================================== 
    // 갤러리 권한을 가지고 있는지 확인하는 메소드
    private fun isGalleryServiceAvaliable() {
        val hasMediaPermission = ContextCompat.checkSelfPermission(
            mContext as Activity,
            android.Manifest.permission.READ_MEDIA_IMAGES
        )

        if (hasMediaPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                REQUIRED_PERMISSIONS,
                REQ_GALLERY
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            imageResult.launch(intent)
        }
    } // End of isGalleryServiceAvaliable


    // 카메라 권한을 가지고 있는지 확인하는 메소드
    private fun isCameraServiceAvaliable(): Boolean {
        val hasCameraPermission = ContextCompat.checkSelfPermission(
            requireContext(), CAM_PERMISSION
        )

        return if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                mContext as MainActivity,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA
            )
            false
        } else {
            // 카메라 권한이 없을경우 요청해서 가져옴
            true
        }
    } // End of isCameraServiceAvaliable

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 가져온 이미지가 있을 경우 해당 데이터를 불러옴.
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult

            val file = File(
                ChangeMultipartUtil().changeAbsoluteyPath(imageUri, requireActivity())
            )
            val exif = ExifInterface(file)

            Picasso.get().load(imageUri).fit().centerCrop()
                .into(binding.stampValidateImageview)

            Log.d(TAG, "exif : $exif")

            // 위경도 좌표가 있을 때만,
            if (exif.latLong != null) {
                val lat = exif.latLong?.get(0)!!
                val lng = exif.latLong?.get(1)!!

                CoroutineScope(Dispatchers.IO).launch {
                    locationCarc(lat, lng)
                    withContext(Dispatchers.Main) {
                        binding.imageLocationInformTextview.text =
                            "Lat : ${lat}, Lon : ${lng}"
                    }
                }
            } else {
                requireView().showSnackBarMessage("해당 이미지의 좌표를 찾을 수 없습니다 다른 이미지를 선택해주세요")
            }

            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("newImageList", file.name, requestFile)
            //newImageList.add(body)
        }
    } // End of registerForActivityResult

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult -> grantResults: $grantResults")
        Log.d(TAG, "onRequestPermissionsResult -> grantResults.size: ${grantResults.size}")

        if (requestCode == REQ_GALLERY && grantResults.size == REQUIRED_PERMISSIONS.size) {
            var checkResult = true

            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false
                    break
                }
            }

            if (!checkResult) {
                requireView().showSnackBarMessage("갤러리 권한을 설정하지 않으면 이미지를 가져올 수 없어요 ㅠㅠ")
            }
        }
    } // End of onRequestPermissionsResult

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                IMAGE_CAPTURE_CODE -> {
                    val file = File(
                        ChangeMultipartUtil().changeAbsoluteyPath(image_uri, requireActivity())
                    )

                    val exif = ExifInterface(file)
                    Picasso.get().load(image_uri).fit().centerCrop()
                        .into(binding.stampValidateImageview)

                    // 위경도 좌표가 있을 때만,
                    if (exif.latLong != null) {
                        val lat = exif.latLong?.get(0)!!
                        val lng = exif.latLong?.get(1)!!

                        CoroutineScope(Dispatchers.IO).launch {
                            locationCarc(lat, lng)
                            withContext(Dispatchers.Main) {
                                binding.imageLocationInformTextview.text =
                                    "Lat : ${lat}, Lon : ${lng}"
                            }
                        }
                    } else {
                        requireView().showSnackBarMessage("해당 이미지의 좌표를 찾을 수 없습니다 다른 이미지를 선택해주세요")
                    }


                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    val body =
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    CoroutineScope(Dispatchers.IO).launch {
                        validateViewModel.imageValidate(body)
                    }
                }
            }
        }
    } // End of onActivityResult


    private suspend fun locationCarc(nowLat: Double, nowLng: Double) {
        var address: String? = null
        val job = CoroutineScope(Dispatchers.Default).async {
            val getAdd = getAddressByCoordinates(nowLat, nowLng)
            if (getAdd != null) {
                address = getAdd.getAddressLine(0)
            }
        }

        job.join()

        val result = address!!.indexOf("Stockholm")
        if (result != -1) {
            // 지역이 일치한다는 조건.
        }
    } // End of getAddress

    private fun getAddressByCoordinates(latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(mContext, Locale.KOREA)

        val addresses: List<Address>?

        addresses = try {
            geocoder.getFromLocation(latitude, longitude, 7)
        } catch (ioException: IOException) {
            binding.root.showSnackBarMessage("지오코더 서비스 사용불가")
            ioException.printStackTrace()
            return null
        } catch (illegalArgumentException: java.lang.IllegalArgumentException) {
            illegalArgumentException.printStackTrace()
            binding.root.showSnackBarMessage("잘못된 위도 경도 입니다.")
            return null
        }

        if (addresses == null || addresses.isEmpty()) {
            binding.root.showSnackBarMessage("주소가 발견되지 않았습니다.")
            return null
        }

        val address: Address = addresses[0]
        Log.d(TAG, "featureName: ${address.featureName}")
        Log.d(TAG, "extras: ${address.extras}")
        Log.d(TAG, "locality: ${address.locality}") // 유력 후보
        // Murmansk // Reykjavík //

        Log.d(TAG, "subAdminArea: ${address.subAdminArea}")
        Log.d(TAG, "maxAddressLineIndex: ${address.maxAddressLineIndex}")

        return address
    } // End of getAddressByCoordinates


    private fun postImageValidateResponseLiveDataObserve() {
        validateViewModel.postImageValidateResponseLiveData.observe(this.viewLifecycleOwner) {
            binding.stampValidateProgressbar.visibility = View.INVISIBLE
            binding.stampValidateProgressbar.isVisible = false
            binding.validateConstraintLayout.visibility = View.VISIBLE
            binding.validateConstraintLayout.isVisible = true

            when (it) {
                is NetworkResult.Success -> {
                    if (it.data == 201) {
                        requireView().showSnackBarMessage("이미지 오로라 맞음!")

                        // 이미지 오로라 맞으면 서버에서 검증 성공 데이터 통신 해야됨
                        // TODO 오로라 검증 성공 데이터 통신부 구현

                    }
                }

                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("이거 오로라 아님!")
                }

                is NetworkResult.Loading -> {
                    binding.stampValidateProgressbar.visibility = View.VISIBLE
                    binding.stampValidateProgressbar.isVisible = true
                    binding.validateConstraintLayout.visibility = View.INVISIBLE
                    binding.validateConstraintLayout.isVisible = false
                }
            }
        }
    } // End of postImageValidate


    companion object {
        private const val REQ_GALLERY = 1
        private const val REQUEST_CAMERA = 3
        private const val REQUEST_STORAGE = 4

        private const val CAM_PERMISSION = android.Manifest.permission.CAMERA
        private const val READ_STORAGE_PERMISSION =
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_STORAGE_PERMISSION =
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
} // End of StampValidateFragment class
