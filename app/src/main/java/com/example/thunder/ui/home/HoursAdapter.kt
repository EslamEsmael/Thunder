package com.example.thunder.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thunder.R
import com.example.thunder.databinding.ItemHourlyRecyclerBinding
import com.example.thunder.model.Hourly
import com.example.thunder.util.UnitsConverters
import java.text.SimpleDateFormat
import java.util.*

class HoursAdapter(var hoursList: MutableList<Hourly>) :

    RecyclerView.Adapter<HoursAdapter.HourlyViewHolder>() {

    val IMG_URL = "https://openweathermap.org/img/wn/"

    lateinit var context: Context

    fun changeData(newList: List<Hourly>) {
        hoursList.clear()
        var list = newList.slice(0..23)
        hoursList.addAll(list)
        notifyDataSetChanged()
    }

    class HourlyViewHolder(var view: ItemHourlyRecyclerBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding =
            ItemHourlyRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return HourlyViewHolder(binding)
    }

    override fun getItemCount(): Int = hoursList.size

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {


        val unitConverters = UnitsConverters(context)

        val item = hoursList[position]
        holder.view.hourTextView.text = getHourAndMinute(item.dt.toLong())

        holder.view.hourTemperatureTextView.text =
            unitConverters.returnTemperatureUsingUserFormat(item.temp.toString())

        Glide.with(holder.view.hourIconImageView.context)
            .load(IMG_URL + item.weather[0].icon + "@2x.png")
            .placeholder(R.drawable.home_fragment_main_circle)
            .into(holder.view.hourIconImageView)
    }

    fun getHourAndMinute(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (timestamp * 1000)
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }
}