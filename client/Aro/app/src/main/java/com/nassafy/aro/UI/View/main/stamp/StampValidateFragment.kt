package com.nassafy.aro.ui.view.main.stamp

import android.app.Activity.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.nassafy.aro.databinding.FragmentStampValidateBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.util.showSnackBarMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //val filePhoto = PhotoFil

        // 사진 찍어서 가져오기
        binding.stampValidateCameraButton.setOnClickListener {
            openCamera()
        }

        // 갤러리에서 가져온 사진.
        binding.stampValidateGalleryButton.setOnClickListener {
            openGallery()
        }

        // 촬영한 이미지의 결과를 가져옴
    } // End of onViewCreated

    private fun openCamera() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_CAMERA)
    } // End of openCamera


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_STORAGE)
    }


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
                REQUEST_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.stampValidateImageview.setImageBitmap(bitmap)
                }
//                REQUEST_STORAGE -> {
//                    val imageUri = result.data?.data ?: return@registerForActivityResult
//                    binding.stampValidateImageview.setImageURI(uri)
//                }
            }
        }

    }

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
            // 지역이 맞는거임
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

    private fun imageCoordinatesCalc() {

    } // End of imageCoordinatesCalc


    companion object {
        private lateinit var photoPath: String
        private val INTENT_CODE = 1
        private val PERMISSION_CODE = 2
        private val REQUEST_CAMERA = 3
        private val REQUEST_STORAGE = 4
    }

} // End of StampValidateFragment class
