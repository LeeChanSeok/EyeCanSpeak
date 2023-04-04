package com.nassafy.aro.ui.view.meteorshower

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.nassafy.aro.R
import com.nassafy.aro.data.dto.MeteorShower
import com.nassafy.aro.databinding.FragmentMeteorShowerBinding
import com.nassafy.aro.ui.adapter.MeteorShowerAdapter
import com.nassafy.aro.ui.view.BaseFragment
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.ui.view.main.MainActivityViewModel

class MeteorShowerFragment :
    BaseFragment<FragmentMeteorShowerBinding>(FragmentMeteorShowerBinding::inflate) {
    private lateinit var meteorShowerAdapter: MeteorShowerAdapter
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private var country = "대한민국"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observe

        // getdata

        if (mainActivityViewModel.meteorShowerServiceEnabled) {
            val mLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            meteorShowerAdapter = MeteorShowerAdapter(itemList)

            binding.meteorShowerCountryTextview.apply {
                text = countryList[0]
//                setOnClickListener {
//                    val meteorCountrySelectDialog = MeteorCountrySelectDialog(
//                        countryList
//                    )
//                    meteorCountrySelectDialog.show(
//                        childFragmentManager, "MeteorCountrySelectDialog"
//                    )
//                }
            }
            binding.drawerImagebutton.setOnClickListener {
                val mainActivity = activity as MainActivity
                mainActivity.openDrawer()
            }
            binding.meteorShowerRecyclerview.apply {
                adapter = meteorShowerAdapter
                layoutManager = mLayoutManager
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            }
            binding.meteorShowerCountryTextview.visibility = View.VISIBLE
            binding.meteorShowerRecyclerview.visibility = View.VISIBLE
            binding.altView.root.visibility = View.GONE
        } else {
            binding.meteorShowerCountryTextview.visibility = View.GONE
            binding.meteorShowerRecyclerview.visibility = View.GONE
            binding.drawerImagebutton.setOnClickListener {
                val mainActivity = activity as MainActivity
                mainActivity.openDrawer()
            }
        }

    } // End of onViewCreated

    companion object {
        var item1 = MeteorShower(
            name = "거문고자리",
            engName = "Lyra",
            date = "2023년 4월 20일",
            image = R.drawable.star_1,
            subImage = R.drawable.star_2
        )
        var item2 = MeteorShower(
            name = "머리털자리",
            engName = "coma berenicids",
            date = "2023년 1월 1일",
            image = R.drawable.star_2,
            subImage = R.drawable.star_2
        )
        var item3 = MeteorShower(
            name = "거문고자리",
            engName = "Lyra",
            date = "2023년 2월 20일",
            image = R.drawable.star_1,
            subImage = R.drawable.star_2
        )
        var item4 = MeteorShower(
            name = "머리털자리",
            engName = "coma berenicids",
            date = "2023년 1월 1일",
            image = R.drawable.star_2,
            subImage = R.drawable.star_2
        )
        var item5 = MeteorShower(
            name = "거문고자리",
            engName = "Lyra",
            date = "2023년 2월 20일",
            image = R.drawable.star_1,
            subImage = R.drawable.star_1
        )
        var item6 = MeteorShower(
            name = "머리털자리",
            engName = "coma berenicids",
            date = "2023년 1월 1일",
            image = R.drawable.star_2,
            subImage = R.drawable.star_2
        )
        var item7 = MeteorShower(
            name = "거문고자리",
            engName = "Lyra",
            date = "2023년 2월 20일",
            image = R.drawable.star_1,
            subImage = R.drawable.star_1
        )
        var itemList = arrayListOf<MeteorShower>(item1, item2, item3, item4, item5, item6, item7)
        var countryList = arrayListOf<String>("대한민국", "아이슬란드", "가나", "일본", "중국", "미국", "영국", "프랑스")
    }
}