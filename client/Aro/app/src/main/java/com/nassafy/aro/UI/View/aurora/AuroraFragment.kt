package com.nassafy.aro.ui.view.aurora

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.PolyUtil
import com.google.maps.android.clustering.ClusterManager
import com.nassafy.aro.BuildConfig
import com.nassafy.aro.R
import com.nassafy.aro.ui.view.ChartAxisFormatter
import com.nassafy.aro.data.dto.PlaceItem
import com.nassafy.aro.data.dto.kp.KpWithProbs
import com.nassafy.aro.databinding.FragmentAuroraBinding
import com.nassafy.aro.ui.adapter.BottomSheetFavoriteAdapter
import com.nassafy.aro.ui.view.*
import com.nassafy.aro.ui.view.dialog.DateHourSelectDialog
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.ui.view.main.MainActivityViewModel
import com.nassafy.aro.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.random.Random

private const val TAG = "AuroraFragment_sdr"

@AndroidEntryPoint
class AuroraFragment : BaseFragment<FragmentAuroraBinding>(FragmentAuroraBinding::inflate),
    OnChartValueSelectedListener,
    OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener,
    ClusterManager.OnClusterItemClickListener<PlaceItem>,
    GoogleMap.OnInfoWindowCloseListener { // End of AuroraFragment
    private val auroraViewModel: AuroraViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    private var mMap: GoogleMap? = null
    private var cloudTileOverlay: TileOverlay? = null
    private lateinit var favoriteAdapter: BottomSheetFavoriteAdapter
    private var now = LocalDateTime.now()
    private var utcNow = LocalDateTime.now(ZoneOffset.UTC)
    private var utcString = utcNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    private var dateList = arrayListOf<String>()
    private var hourList = arrayListOf<ArrayList<String>>()
    private var chartHourLabel = getChartHourLabel(now, now)
    private var kpIndex = 0.0
    private var kpWithProbs = KpWithProbs()
    private var mPolyline : Polyline? = null

    private lateinit var mClusterManager: ClusterManager<PlaceItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()

        Log.d(TAG, "onViewCreated: $utcString, ${utcNow.hour}")
        getData(utcString, utcNow.hour)

        initView()

        initBottomSheetChart(chartHourLabel, kpWithProbs.kps)

        initBottomSheetRecyclerView()

        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment

        OnMapAndViewReadyListener(mapFragment, this)

    } // End of onViewCreated

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap!!.uiSettings.isMapToolbarEnabled = false
        setCustomMapStyle()

        // setCloudTileOverlay
        when {
            mainActivityViewModel.cloudDisplayOption -> {
                setCloudTileOverlay()
            }
            mainActivityViewModel.cloudDisplayOption == false && cloudTileOverlay != null -> {
                cloudTileOverlay!!.remove()
            }
            else -> {}
        }

        // setPolyLine
        when {
            mainActivityViewModel.auroraDisplayOption -> {
                setPolyLine()
            }
            mainActivityViewModel.auroraDisplayOption == false && mPolyline != null -> {
                mPolyline!!.remove()
            }
            else -> {}
        }

        // set ClusterManager
        setClusterManager()
    } // End of onMapReady

    private fun initObserve() {
        auroraViewModel.currentKpIndexLiveData.observe(viewLifecycleOwner) {
            kpIndex = if (it.data != null) {
                it.data!!.kp
            } else {
                -1.0
            }
        } // End of currentKpIndexLiveData

        auroraViewModel.kpAndProbsLiveData.observe(viewLifecycleOwner) {
            if (it.data != null) {
                kpWithProbs = it.data!!
                favoriteAdapter.probs = it.data!!.probs
                favoriteAdapter.notifyDataSetChanged()
                initBottomSheetChart(chartHourLabel, it.data!!.kps)
                binding.bottomSheet.kpLinechart.notifyDataSetChanged()
                binding.bottomSheet.kpLinechart.invalidate()
            }
        } // End of kpAndProbsLiveData

        auroraViewModel.placeItemListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (mMap != null && it.data != null) {
                        mClusterManager.clearItems()
                        mClusterManager.addItems(it.data)
                        mClusterManager.cluster()
                        // set Start Location
                        mMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(64.8, -18.5),
                                5F
                            )
                        )
                    }
                }
                is NetworkResult.Error -> {
                    requireView().showSnackBarMessage("서버 통신 에러 발생")
                }
                is NetworkResult.Loading -> {
                    requireView().showSnackBarMessage("로딩 중")
                }
            }
        } // End of placeItemListLiveData
    }

    private fun getData(dateString: String, hour: Int) {
        auroraViewModel.getCurrentKpIndex(dateString, hour)
        auroraViewModel.getKpAndProbsLiveData(dateString, hour)
        auroraViewModel.getPlaceItemList(dateString, hour)
    }

    private fun initView() {
        dateList = getDateList(now)
        hourList = getHourList(dateList, now)

        binding.drawerImagebutton.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.openDrawer()
        }

        binding.dateTextview.text = LocalDate.of(now.year, now.month, now.dayOfMonth).format(
            DateTimeFormatter.ofPattern("yy.MM.dd")
        )

        binding.hourTextview.text =
            LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, 0).format(
                DateTimeFormatter.ofPattern("HH:mm")
            )

        binding.dateHourLinearlayout.setOnClickListener {
            val dateHourSelectDialog = DateHourSelectDialog(dateList, hourList)
            closeBottomSheet()
            dateHourSelectDialog.show(
                childFragmentManager, "DateHourSelectDialog"
            )
        }
    } // End of initView

    private fun initBottomSheetChart(chartHourLabel: ArrayList<String>, kps: List<Double>) {
        val kpLineChart = binding.bottomSheet.kpLinechart

        val kpValues = arrayListOf<Entry>()
        if (kps.size == 24) {
            for (i: Int in 0..23) {
                var tempValue = kpWithProbs.kps[i].toFloat()
                var roundValue: Float = (tempValue * 100).roundToInt().toFloat() / 100
                kpValues.add(Entry(i.toFloat(), roundValue))
            }
        } else {
            for (i: Int in 0..23) {
                kpValues.add(Entry(i.toFloat(), 0.0F))
            }
        }

        val kpDataSet = LineDataSet(kpValues, "DataSet")
        val kpDataSets = arrayListOf<ILineDataSet>(kpDataSet)
        val lineData = LineData(kpDataSets)

        // When Chart Marker Selected
        val myMarkerView = ChartMarkerView(requireContext(), R.layout.bottom_sheet_chart_marker)
        myMarkerView.chartView = kpLineChart

        // Set ChartData Appearance
        kpDataSet.apply {
            lineWidth = 2.5F
            color = Color.rgb(75, 181, 117)
            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)
            setDrawValues(false)
            setDrawCircles(false)
        }

        // Set Chart Appearance
        kpLineChart.apply {
            data = lineData
            description.isEnabled = false
            setPinchZoom(false)
            isDragEnabled = false
            isDoubleTapToZoomEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false
            setOnChartValueSelectedListener(this@AuroraFragment)
            setDrawGridBackground(false)
            marker = myMarkerView

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.WHITE
                valueFormatter = ChartAxisFormatter(
                    chartHourLabel
                )
            } // End of xAxis

            axisLeft.apply {
                textColor = Color.WHITE
                axisMinimum = 0.0F
                axisMaximum = 9.0F
                labelCount = 8
                valueFormatter = ChartAxisFormatter(
                    arrayListOf("", "1", "2", "3", "4", "5", "6", "7", "8", "9")
                )
            } // End of axisLeft (yAxis)
        }
    } // End of initBottomSheetChart

    private fun initBottomSheetRecyclerView() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet.root)

        favoriteAdapter = BottomSheetFavoriteAdapter(kpWithProbs.probs)

        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager(requireContext()).orientation
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.line_divider
            )!!
        )

        binding.bottomSheet.favoriteRecyclerview.apply {
            adapter = favoriteAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(dividerItemDecoration)
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        when (newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> {
                                behavior.isDraggable = true
                            }
                            RecyclerView.SCROLL_STATE_DRAGGING -> {
                                behavior.isDraggable = false
                            }
                            RecyclerView.SCROLL_STATE_SETTLING -> {
                                behavior.isDraggable = false
                            }
                        }
                        super.onScrollStateChanged(recyclerView, newState)
                    } // End of onScrollStateChanged
                })
        } // End of favoriteRecyclerView

    } // End of initBottomSheetRecyclerView

    fun changeDateTime(date: String, hour: String) {
        binding.dateTextview.text = date
        binding.hourTextview.text = hour

        val formatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm")
        var selectedDate = LocalDateTime.parse("$date $hour", formatter)
        utcNow = selectedDate.atZone(ZoneId.systemDefault())
                                    .withZoneSameInstant(ZoneId.of("UTC"))
                                    .toLocalDateTime()

        utcString = utcNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        Log.d(TAG, "changeDateTime: $utcString, ${utcNow.hour}")

        getData(utcString, utcNow.hour)
    } // End of setDateTimeLinearLayoutText

    private fun closeBottomSheet() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    } // End of closeBottomSheet

    private fun setCustomMapStyle() {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = mMap!!.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.map_night_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    } // End of setCustomMapStyle

    private fun setCloudTileOverlay() {
        val tileProvider: TileProvider = object : UrlTileProvider(256, 256) {
            override fun getTileUrl(x: Int, y: Int, z: Int): URL? {
                val s =
                    "https://tile.openweathermap.org/map/clouds_new/${z}/${x}/${y}.png?appid=${BuildConfig.WEATHER_API_KEY}"
                val tileUrl: URL? = try {
                    URL(s)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }
//                Log.d(TAG, "getTileUrl: $tileUrl")
                return tileUrl
            }
        }
        cloudTileOverlay = mMap!!.addTileOverlay(
            TileOverlayOptions()
                .tileProvider(tileProvider)
                .transparency(0.9f)
        )!!
    } // End of setCloudTileOverlay

    private fun setPolyLine() {
        val polylineOptions = getKpPolylineOptions(kpIndex)
        mPolyline = mMap!!.addPolyline(polylineOptions)
        mMap!!.setOnMapClickListener { latLng ->
            // When Clicked Location is on Polyline, Google Map shows Info.
            val tolerance = getKpPolylineTolerance(mMap!!.cameraPosition.zoom)
            if (PolyUtil.isLocationOnPath(latLng, polylineOptions.points, true, tolerance)) {
                mPolyline!!.addInfoWindow(mMap!!, latLng, "KP 지수", "${round(kpIndex * 100) / 100}")
            }
        } // End of setOnMapClickListener
    } // End of setPolyLine

    private fun setClusterManager() {
        mClusterManager = ClusterManager<PlaceItem>(requireContext(), mMap)
        mMap!!.setOnCameraIdleListener(mClusterManager)
        mClusterManager.renderer = CustomMarkerRenderer(requireContext(), mMap!!, mClusterManager)
        mClusterManager.markerCollection.setInfoWindowAdapter(
            CustomMarkerInfoRenderer(
                layoutInflater,
                requireContext(),
                auroraViewModel
            )
        )
        mClusterManager.setOnClusterItemClickListener(this@AuroraFragment)

//        auroraViewModel.getPlaceItemList()
    } // End of setClusterManager

    // override OnChartValueSelectedListener
    override fun onValueSelected(e: Entry?, h: Highlight?) {
    } // End of onValueSelected

    // override OnChartValueSelectedListener
    override fun onNothingSelected() {
    } // End of onNothingSelected

    override fun onClusterItemClick(item: PlaceItem?): Boolean {
        return false
    } // End of onClusterItemClick

    override fun onInfoWindowClose(marker: Marker) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::mClusterManager.isInitialized) {
            mClusterManager.clearItems()
            mClusterManager.cluster()
            mMap = null
        }
    }
}