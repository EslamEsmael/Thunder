package com.example.thunder.ui.favourites

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.thunder.R
import com.example.thunder.databinding.FragmentFavouritesBinding
import com.example.thunder.databinding.FragmentHomeBinding
import com.example.thunder.databinding.FragmentPlaceDetailsBinding
import com.example.thunder.model.WeatherResponse
import com.example.thunder.ui.home.DailyAdapter
import com.example.thunder.ui.home.HomeViewModel
import com.example.thunder.ui.home.HoursAdapter
import com.example.thunder.util.UnitsConverters
import com.example.thunder.util.capitalizeWords
import com.example.thunder.util.isOnline
import java.util.*

class PlaceDetailsFragment : Fragment() {

    private var _binding: FragmentPlaceDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    lateinit var address: List<Address>
    lateinit var geocoder: Geocoder
    lateinit var weatherResponse: WeatherResponse

    private lateinit var hoursAdapter: HoursAdapter
    private lateinit var daysAdapter: DailyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            Log.d("TAG", "onCreate: Eslam")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        geocoder = Geocoder(requireContext(), Locale.getDefault())

        hoursAdapter = HoursAdapter(arrayListOf())
        daysAdapter = DailyAdapter(arrayListOf())

        binding.hourlyRecyclerView.apply {
            adapter = hoursAdapter
        }
        binding.daysRecyclerView.apply {
            adapter = daysAdapter
        }


        val data = arguments?.get("current") as WeatherResponse
        setUI(data)

        return root
    }

    private fun setUI(weatherResponse: WeatherResponse) {

        if (isOnline(requireContext())) {
            val address = geocoder.getFromLocation(weatherResponse.lat, weatherResponse.lon, 1)
            binding.cityNameTextView.text = address[0].subAdminArea
            binding.countryNameTextView.text = address[0].countryName
        }
        val converters = UnitsConverters(requireContext())

        binding.temperatureTextView.text =
            converters.returnTemperatureUsingUserFormat(weatherResponse.current.temp.toString())

        binding.humidityValueTextView.text = weatherResponse.current.humidity.toString() + " %"

        binding.pressureValueTextView.text = weatherResponse.current.pressure.toString() + " hPa"

        binding.windSpeedTextView.text =
            converters.returnWindSpeedUsingUserFormat(weatherResponse.current.wind_speed.toString())

        binding.weatherStatusTextView.text =
            weatherResponse.hourly!![0].weather[0].description.capitalizeWords()
        hoursAdapter.changeData(weatherResponse.hourly)
        daysAdapter.changeData(weatherResponse.daily!!)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}