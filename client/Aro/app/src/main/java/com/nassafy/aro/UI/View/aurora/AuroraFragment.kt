package com.nassafy.aro.ui.view.aurora

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
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
import com.nassafy.aro.databinding.FragmentAuroraBinding
import com.nassafy.aro.ui.adapter.BottomSheetFavoriteAdapter
import com.nassafy.aro.ui.view.*
import com.nassafy.aro.ui.view.dialog.DateHourSelectDialog
import com.nassafy.aro.ui.view.main.MainActivity
import com.nassafy.aro.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    private var mMap: GoogleMap? = null
    private lateinit var cloudTileOverlay: TileOverlay
    private lateinit var favoriteAdapter: BottomSheetFavoriteAdapter
    private var now = LocalDateTime.now()
    private var dateList = arrayListOf<String>()
    private var hourList = arrayListOf<ArrayList<String>>()
    private var chartHourLabel = getChartHourLabel(now, now)

    private lateinit var mClusterManager: ClusterManager<PlaceItem>

    var kpIndex = 9.0F

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initBottomSheetChart(chartHourLabel)

        initBottomSheetRecyclerView()

        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment

        OnMapAndViewReadyListener(mapFragment, this)
    } // End of onViewCreated

    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap!!
        mMap!!.uiSettings.isMapToolbarEnabled = false
        setCustomMapStyle()
//        setCloudTileOverlay()

        // setPolyLine
        val polylineOptions = getKpPolylineOptions(kpIndex)
        val polyline = mMap!!.addPolyline(polylineOptions)
        mMap!!.setOnMapClickListener { latLng ->
            auroraViewModel.setClickedLocation(latLng)

            // When Clicked Location is on Polyline, Google Map shows Info.
            val tolerance = getKpPolylineTolerance(mMap!!.cameraPosition.zoom)
            if (PolyUtil.isLocationOnPath(latLng, polylineOptions.points, true, tolerance)) {
                polyline.addInfoWindow(mMap!!, latLng, "KP 지수", "$kpIndex")
            }
        } // End of setOnMapClickListener

        // set ClusterManager
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

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                auroraViewModel.getPlaceItemList()
            }
        }

        auroraViewModel.placeItemListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (mMap != null && it.data != null) {
                        mClusterManager.addItems(it.data)
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
        }
    } // End of onMapReady

    private fun initView() {
        dateList = getDateList(now)
        hourList = getHourList(dateList, now)

        binding.drawerImagebutton.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.openDrawer()
        }

        binding.dateTextview.text = LocalDate.of(now.year, now.month, now.dayOfMonth).format(
            DateTimeFormatter.ofPattern("yy/MM/dd")
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

    private fun initBottomSheetChart(chartHourLabel: ArrayList<String>) {
        var kpLineChart = binding.bottomSheet.kpLinechart

        // Dummy Data
        var kpValues = arrayListOf<Entry>()
        val min = 0.0f
        val max = 9.0f

        for (i: Int in 0..23) {
            val randomFloat = min + (max - min) * Random.nextFloat()
            val temp = (randomFloat * 1000.0).roundToInt() / 1000.0
            kpValues.add(Entry(i.toFloat(), temp.toFloat()))
        }

        var kpDataSet = LineDataSet(kpValues, "DataSet")
        var kpDataSets = arrayListOf<ILineDataSet>(kpDataSet)
        var lineData = LineData(kpDataSets)

        // When Chart Marker Selected
        var myMarkerView = ChartMarkerView(requireContext(), R.layout.bottom_sheet_chart_marker)
        myMarkerView.chartView = kpLineChart

        // Set ChartData Appearance
        kpDataSet.apply {
            lineWidth = 2.5F
//            circleRadius = 4.5F
//            circleHoleRadius = 1.5F
//            setCircleColor(Color.rgb(75, 181, 117))
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

        favoriteAdapter = BottomSheetFavoriteAdapter(itemList)

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

        val formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm")
        var selectedDate = LocalDateTime.parse("$date $hour", formatter)
        initBottomSheetChart(getChartHourLabel(selectedDate, now))
        binding.bottomSheet.kpLinechart.notifyDataSetChanged()
        binding.bottomSheet.kpLinechart.invalidate()

        Log.d(TAG, "changeDateTime: ${getChartHourLabel(selectedDate, now)}")

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
                Log.d(TAG, "getTileUrl: $tileUrl")
                return tileUrl
            }
        }
        cloudTileOverlay = mMap!!.addTileOverlay(
            TileOverlayOptions()
                .tileProvider(tileProvider)
                .transparency(0.9f)
        )!!
    } // End of setCloudTileOverlay

    private fun getCurrentWeather(lat: Float, lng: Float) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                auroraViewModel.getCurrentWeather(lat, lng)
            }
        }
    }

    // Dummy Data
    companion object {
        var item1 = arrayListOf<String>("그리핀도르", "88", "Thunderstorm")
        var item2 = arrayListOf<String>("슬리데린", "40", "Drizzle")
        var item3 = arrayListOf<String>("레벤클로", "20", "Rain")
        var item4 = arrayListOf<String>("후플프푸", "10", "Snow")
        var item5 = arrayListOf<String>("레이캬비크", "50", "Atmosphere")
        var item6 = arrayListOf<String>("신도림", "49", "Clear")
        var item7 = arrayListOf<String>("구미", "100", "Clouds")
        var itemList =
            arrayListOf<MutableList<String>>(item1, item2, item3, item4, item5, item6, item7)

//        val reykjavik = Place(0, "아이슬란드", "레이캬비크",64.133F, -21.933F, "https://i.pinimg.com/564x/0b/4c/83/0b4c8348f7e0e89fc9089d0d5b320d68.jpg")
    }

    // override OnChartValueSelectedListener
    override fun onValueSelected(e: Entry?, h: Highlight?) {
    } // End of onValueSelected

    // override OnChartValueSelectedListener
    override fun onNothingSelected() {
    } // End of onNothingSelected

    override fun onClusterItemClick(item: PlaceItem?): Boolean {
        if (item != null) {
            Log.d(TAG, "onClusterItemClick: hi")
            getCurrentWeather(item.longitude, item.longitude)
        }
        return false
    } // End of onClusterItemClick

    override fun onInfoWindowClose(marker: Marker) {
//        Log.d(TAG, "onInfoWindowClose: ${auroraViewModel.currentWeatherLiveData.hasActiveObservers()}")
        Log.d(TAG, "onInfoWindowClose: ${marker.title}")


//        auroraViewModel.currentWeatherLiveData.removeObserver(
//            
//        )
    }

}