package com.nassafy.aro.ui.view.main.stamp

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
import androidx.exifinterface.media.ExifInterface
import com.nassafy.aro.databinding.FragmentStampValidateBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.ChangeMultipartUtil
import com.nassafy.aro.util.showSnackBarMessage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.*

private const val TAG = "StampValidateFragment_싸피"

class StampValidateFragment :
    BaseFragment<FragmentStampValidateBinding>(FragmentStampValidateBinding::inflate) {
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    } // End of onAttach

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사진 찍어서 가져오기
        binding.stampValidateCameraButton.setOnClickListener {
            Log.d(TAG, "onViewCreated:  버튼 여기 동작하나요?")
            if (isCameraServiceAvaliable()) {
                openCam()
            }
        }

        // 갤러리에서 가져온 사진.
        binding.stampValidateGalleryButton.setOnClickListener {
            // 갤러리도 권한을 확인해서 열어야함
        }
    } // End of onViewCreated

    var image_uri: Uri? = null
    private val RESULT_LOAD_IMG = 123
    val IMAGE_CAPTURE_CODE = 654

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
    }


    // 카메라 열기, 권한이 없으면 터지므로 권한 설정 해줘야됨.
    // 권한이 없으면 권한을 받을 때 까지 카메라를 열 수 없음.
    private fun openCamera() {
        Log.d(TAG, "openCamera: 여기 동작함")
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camIntent, REQUEST_CAMERA)
    } // End of openCamera

    private fun isCameraServiceAvaliable(): Boolean {
        Log.d(TAG, "isCameraServiceAvaliable: 권한체크 동작하나요?")

        val hasCameraPermission = ContextCompat.checkSelfPermission(
            requireContext(), CAM_PERMISSION
        )

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireContext() as MainActivity,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA
            )
        } else {
            // 카메라 권한이 없을경우 요청해서 가져옴
            return true
        }

        return false
    } // End of isCameraServiceAvaliable


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_STORAGE)
    } // End of openGallery

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA) {
            // 카메라 권한이 있으면 카메라를 실행시킴.

            var permissionCheckflg = false
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    permissionCheckflg = true
                }
            }

            Log.d(TAG, "onRequestPermissionsResult: 여기 동작함??")

            if (permissionCheckflg) {
                openCamera()
            } else {
                requireView().showSnackBarMessage("권한을 허용해야만 카메라를 사용할 수 있습니다.")
            }
        }
    } // End of onRequestPermissionsResult

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
                        MultipartBody.Part.createFormData("newImageList", file.name, requestFile)
                }
            }
        }
    } // End of onActivityResult

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 가져온 이미지가 있을 경우 해당 데이터를 불러옴.
//        if (result.resultCode == Activity.RESULT_OK) {
//            val imageUri = result.data?.data ?: return@registerForActivityResult
//
//            val file = File(
//                ChangeMultipartUtil().changeAbsoluteyPath(imageUri, requireActivity())
//            )
//
//            // 이미지 띄우기
//            Picasso.get().load(imageUri).fit().centerCrop().into(binding.stampValidateImageview)
//
//
//            val exif: ExifInterface = ExifInterface(file)
//
//            // 위경도 좌표가 있을 때만,
//            if (exif.latLong != null) {
//                val lat = exif.latLong?.get(0)!!
//                val lng = exif.latLong?.get(1)!!
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    locationCarc(lat, lng)
//                    withContext(Dispatchers.Main) {
//                        binding.imageMetadataTextview.text = "Lat : ${lat}, Lon : ${lng}"
//                    }
//                }
//            } else {
//                requireView().showSnackBarMessage("해당 이미지의 좌표를 찾을 수 없습니다 다른 이미지를 선택해주세요")
//            }
//            // 이미지의 메타데이터에서 위도, 경도를 가져와서 해당 위치의 오차범위를 계산해서 옳은지 판단.
//
//
////            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
////            val body =
////                MultipartBody.Part.createFormData("newImageList", file.name, requestFile)
//        }
    } // End of registerForActivityResult

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


    companion object {
        private lateinit var photoPath: String
        private val INTENT_CODE = 1
        private val PERMISSION_CODE = 2
        private val REQUEST_CAMERA = 3
        private val REQUEST_STORAGE = 4

        private const val PERMISSION_REQUEST_CODE = 100

        private const val CAM_PERMISSION = android.Manifest.permission.CAMERA
        private const val READ_STORAGE_PERMISSION =
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_STORAGE_PERMISSION =
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

} // End of StampValidateFragment class
