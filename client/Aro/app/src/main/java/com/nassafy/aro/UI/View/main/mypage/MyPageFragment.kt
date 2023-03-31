package com.nassafy.aro.ui.view.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.databinding.FragmentMyPageBinding
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.custom.NanumSqaureFont
import com.nassafy.aro.ui.view.dialog.OkDialog
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.ui.view.main.MainActivityViewModel
import com.nassafy.aro.ui.view.main.mypage.viewmodel.MyPageFragmentViewModel
import com.nassafy.aro.util.NetworkResult
import com.nassafy.aro.util.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val myPageFragmentViewModel: MyPageFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val main = requireActivity() as MainActivity
        main.closeDrawer()
    } // End of onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        CoroutineScope(Dispatchers.IO).launch {
            myPageFragmentViewModel.getSelectedServiceList()
        } // End of CoroutineScope
        initView()
    } // End of onViewCreated

    private fun initObserve() {
        myPageFragmentViewModel.nicknameLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    mainActivityViewModel.nickname = binding.userNicknameEdittext.text.toString()
                    binding.nicknameChangeSaveAlertImagebutton.isVisible = true
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        binding.progressBar.isVisible = false
                        binding.nicknameChangeSaveAlertImagebutton.isGone = true
                    }
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("닉네임 재설정에 실패했습니다.")
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            } // End of when
        } // End of nicknameLiveData.observe

        myPageFragmentViewModel.getSelectedServiceNetworResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFragmentViewModel.auroraService = it.data!!.auroraService
                    myPageFragmentViewModel.meteorService = it.data!!.meteorService
                    when (it.data!!.auroraService || it.data!!.meteorService) {
                        true -> {
                            binding.serviceSelectedGroup.isVisible = true
                            binding.serviceNotSelectedGroup.isVisible = false
                            initComposeView()
                        }
                        false -> {
                            binding.serviceSelectedGroup.isVisible = false
                            binding.serviceNotSelectedGroup.isVisible = true
                        }
                    } // End of when
                    binding.progressBar.isVisible = false
                } // End of NetworkResult.Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("닉네임 재설정에 실패했습니다.")
                } // End of NetworkResult.Error
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                } // End of NetworkResult.Loading
            } // End of when
        } // End of getSelectedServiceNetworResultLiveData.observe

        myPageFragmentViewModel.favoriteListNetworkResultLiveData.observe(this.viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    myPageFragmentViewModel.favoriteAuroraPlaceList.clear()
                    myPageFragmentViewModel.favoriteAuroraPlaceList.addAll(
                        it.data?.attractionInterestOrNotDTOList ?: emptyList()
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(800)
                        binding.progressBar.isVisible = false
                    }
//                    myPageFragmentViewModel.favoriteMeteorPlaceList.addAll(it.data?.memteorInterestOrNotDTO!!)
                }// End of NetworkResult.Success
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("닉네임 재설정에 실패했습니다.")
                } // End of NetworkResult.Error
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                } // End of NetworkResult.Loading
            } // End of when
        } // End of favoriteListNetworkResultLiveData.observe

        myPageFragmentViewModel.deleteFavoriteNetworkResultLiveData.observe(this.viewLifecycleOwner) { result ->
            when(result) {
                is NetworkResult.Success -> {
                    myPageFragmentViewModel.favoriteAuroraPlaceList.removeAll { it.interestId.equals(result.data!!) }
                    binding.progressBar.isVisible = false
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("관심지역 삭제에 실패했습니다.")
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            } // End of when
        } // End of deleteFavoriteNetworkResultLiveData.observe

    } // End of initObserve

    private fun initView() {
        binding.userNicknameEdittext.setText(mainActivityViewModel.nickname)
        binding.userEmailTextview.text = mainActivityViewModel.email
        binding.nicknameChangeImagebutton.apply {
            setOnClickListener {
                when (isSelected) {
                    true -> {// 재설정 로직
                        val afterChangedNickname = binding.userNicknameEdittext.text.toString()
                        when (mainActivityViewModel.nickname != afterChangedNickname) {
                            true -> {
                                when (afterChangedNickname.length in 3..10) {
                                    true -> {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            myPageFragmentViewModel.changeNickname(binding.userNicknameEdittext.text.toString())
                                        }
                                    }
                                    false -> {
                                        val okDialog = OkDialog(
                                            getString(R.string.my_page_nickname_modify_error_dialog_title_textview_text),
                                            getString(R.string.my_page_nickname_modify_error_dialog_inform_textview_text),
                                            getString(R.string.dialog_confirm_button_text)
                                        )
                                        okDialog.show(
                                            childFragmentManager, "OkDialog"
                                        )
                                        binding.userNicknameEdittext.setText(
                                            mainActivityViewModel.nickname
                                        )
                                    }
                                } // End of afterChangedNickname.length in 3..10
                            } // End of isSelected == true
                            false -> {}
                        }  // End of when(mainActivityViewModel.nickname != binding.userNicknameEdittext.text.toString())
                    } // End of isSelected == true
                    false -> {} // End of isSelected == false
                } // End of when(isSelected)
                isSelected = !isSelected
                binding.userNicknameEdittext.isEnabled = isSelected
            }
        } // End of initView
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        } // End of cancelButton.setOnClickListener
        binding.serviceRegistButton.setOnClickListener {
            moveToServiceRegisterFragment()
        } // End of serviceRegistButton.setOnClickListener
        binding.myFavoriteAddImageButton.setOnClickListener {
            moveToFavoriteRegisterFragment()
        } // End of myFavoriteAddImageButton.setOnClickListener
        binding.serviceModifyButton.setOnClickListener {
            moveToServiceRegisterFragment()
        } // End of serviceModifyButton.setOnClickListener
    } // End of initVIew

    fun moveToServiceRegisterFragment() {
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageServiceRegisterFragment(
            myPageFragmentViewModel.auroraService,
            myPageFragmentViewModel.meteorService,
        )
        findNavController().navigate(action)
    } // End of moveToServiceRegisterFragment

    fun moveToFavoriteRegisterFragment() {
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageFavoriteRegisterFragment(
            myPageFragmentViewModel.auroraService,
            myPageFragmentViewModel.meteorService,
        )
        findNavController().navigate(action)
    } // End of moveToFavoriteRegisterFragment

    @OptIn(ExperimentalPagerApi::class)
    fun initComposeView() {
        CoroutineScope(Dispatchers.IO).launch {
            myPageFragmentViewModel.getFavoriteList()
        } // End of CoroutineScope
        binding.myFavoriteComposeview.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val auroraFavoriteList =
                    remember { myPageFragmentViewModel.favoriteAuroraPlaceList }
                val memeorFavoriteList = mutableListOf<PlaceItem>()
                // In Compose world

                LaunchedEffect(myPageFragmentViewModel.favoriteAuroraPlaceList) {
                    auroraFavoriteList.clear()
                    auroraFavoriteList.addAll(myPageFragmentViewModel.favoriteAuroraPlaceList)
                }

                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .height(this.height.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HorizontalPager(count = 2) {page ->
                            when (page) {
                                0 -> {
                                    MyAuroraFavorite(auroraFavoriteList = auroraFavoriteList, myPageFragmentViewModel = myPageFragmentViewModel)
                                }
                                1 -> {
                                    // Todo
                                }
                            }
                        }
                    } // End of Column
                } // End of MaterialTheme
            } // End of setContent
        } // ENd of binding.myFavoriteComposeview.apply
    } // End of initComposeView
} // End of MyPageFragment