package com.example.thunder.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.thunder.databinding.FragmentHomeBinding
import com.example.thunder.model.WeatherResponse
import com.example.thunder.util.UnitsConverters
import com.example.thunder.util.WeatherUtil
import com.example.thunder.util.capitalizeWords
import com.example.thunder.util.isOnline
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class HomeFragment : Fragment() {

    val PERMISSION_REQUEST_ACCESS_LOCATION = 42

    private var currentLon: Double = 0.0
    private var currentLat: Double = 0.0
    lateinit var address: List<Address>
    lateinit var geocoder: Geocoder
    var currentCity: String = "City"
    lateinit var weatherResponse: WeatherResponse

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var hoursAdapter: HoursAdapter
    private lateinit var daysAdapter: DailyAdapter

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        geocoder = Geocoder(requireContext(), Locale.getDefault())

        hoursAdapter = HoursAdapter(arrayListOf())
        daysAdapter = DailyAdapter(arrayListOf())

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.animationView.visibility = View.VISIBLE

        if (isOnline(requireContext())) {
            CoroutineScope(Dispatchers.IO).launch {
                getLastKnownLocation()
            }
        } else {
            //there is no internet Connection
            binding.animationView.visibility = View.GONE
            Toast.makeText(requireContext(), "No internet connection Available", Toast.LENGTH_LONG)
                .show()

            viewModel.getSavedCurrentPlace().asLiveData().observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    setUI(it[0])
                } else {
                    binding.noDataAvailableRelativeLayout.visibility = View.VISIBLE
                }
            }
        }

        binding.hourlyRecyclerView.apply {
            adapter = hoursAdapter
        }
        binding.daysRecyclerView.apply {
            adapter = daysAdapter
        }

        return root
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.awaitResult().let {
                    if (it == null) {
                        forceRequestLocation()
                        Log.d("Loc", "getLastLocation: ")
                    } else {
                        currentLat = it.latitude
                        currentLon = it.longitude
                        //todo uncomment geoCoder
                        address = geocoder.getFromLocation(currentLat, currentLon, 1)
                        currentCity = address[0].subAdminArea
                        viewLifecycleOwner.lifecycleScope.launch {
                            weatherResponse =
                                viewModel.makeRequest(currentLat, currentLon, currentCity)
                                    .body()!!
                            setUI(weatherResponse)
                        }
                    }
                }
            } else {
                locationNotEnable()
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUI(weatherResponse: WeatherResponse) {

        lifecycle.coroutineScope.launch {
            viewModel.saveCurrentLocationWeatherResponse(weatherResponse)
        }

        val address = geocoder.getFromLocation(weatherResponse.lat, weatherResponse.lon, 1)
        val converters = UnitsConverters(requireContext())
        binding.cityNameTextView.text = address[0].subAdminArea

        binding.temperatureTextView.text =
            converters.returnTemperatureUsingUserFormat(weatherResponse.current.temp.toString())

        binding.humidityValueTextView.text =
            weatherResponse.current.humidity.toString() + " %"

        binding.pressureValueTextView.text =
            weatherResponse.current.pressure.toString() + " hPa"

        binding.windSpeedTextView.text =
            converters.returnWindSpeedUsingUserFormat(weatherResponse.current.wind_speed.toString())

        binding.weatherStatusTextView.text =
            weatherResponse.hourly!![0].weather[0].description.capitalizeWords()
        hoursAdapter.changeData(weatherResponse.hourly)
        daysAdapter.changeData(weatherResponse.daily!!)


        binding.animationView.visibility = View.GONE
        binding.noDataAvailableRelativeLayout.visibility = View.GONE
        binding.mainViewConstrainLayout.visibility = View.VISIBLE
    }

    private fun forceRequestLocation() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        Looper.myLooper()?.let {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                it
            )
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
        }
    }


    private suspend fun <T> Task<T>.awaitResult() = suspendCoroutine<T?> { continuation ->
        if (isComplete) {
            if (isSuccessful) continuation.resume(result)
            else continuation.resume(null)
            return@suspendCoroutine
        }
        addOnSuccessListener { continuation.resume(result) }
        addOnFailureListener { continuation.resume(null) }
        addOnCanceledListener { continuation.resume(null) }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun locationNotEnable() {

        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        ActivityCompat.startActivityForResult(
            requireActivity(),
            intent,
            PERMISSION_REQUEST_ACCESS_LOCATION,
            Bundle()
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_ACCESS_LOCATION
        )

        Log.d("TAG", "requestPermissions: ")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                lifecycle.coroutineScope.launch {
                    getLastKnownLocation()
                }

            }

        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}