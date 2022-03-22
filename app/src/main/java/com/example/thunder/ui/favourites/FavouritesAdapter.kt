package com.example.thunder.ui.favourites

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thunder.R
import com.example.thunder.databinding.ItemFavouritesBinding
import com.example.thunder.model.WeatherResponse
import com.example.thunder.util.Constants
import com.example.thunder.util.UnitsConverters
import java.util.*

class FavouritesAdapter(
    var favList: MutableList<WeatherResponse>,
    val operation: (View, WeatherResponse) -> Unit
) :
    RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {

    private lateinit var context: Context
    private lateinit var geocoder: Geocoder

    class FavouritesViewHolder(var view: ItemFavouritesBinding) :
        RecyclerView.ViewHolder(view.root)

    @SuppressLint("NotifyDataSetChanged")
    fun changeData(newList: List<WeatherResponse>) {
        favList.clear()
        favList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesViewHolder {


        val binding =
            ItemFavouritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        geocoder = Geocoder(context, Locale.getDefault())

        return FavouritesViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val item = favList[position]

        val address: List<Address> = geocoder.getFromLocation(item.lat, item.lon, 1)
        val unitsConverters = UnitsConverters(context)

        holder.view.root.setOnClickListener() {
            operation(holder.view.root, item)
        }

        holder.view.cityNameTextView.text = address[0].subAdminArea
        holder.view.countryNameTextView.text = address[0].countryName

        holder.view.temperatureTextView.text =
            unitsConverters.returnTemperatureUsingUserFormat(item.current.temp.toString())

        holder.view.humidityValueTextView.text = item.current.humidity.toString() + " %"

        holder.view.windSpeedTextView.text =
            unitsConverters.returnWindSpeedUsingUserFormat(item.current.wind_speed.toString())

        Glide.with(holder.view.weatherStatusImageView.context)
            .load(Constants.imageUrl + item.daily!![0].weather[0].icon + "@2x.png")
            .placeholder(R.drawable.home_fragment_main_circle)
            .into(holder.view.weatherStatusImageView)

    }

    override fun getItemCount() = favList.size
}