package com.example.thunder.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thunder.R
import com.example.thunder.databinding.ItemDailyRecyclerBinding
import com.example.thunder.databinding.ItemHourlyRecyclerBinding
import com.example.thunder.model.Daily
import com.example.thunder.model.Hourly
import com.example.thunder.util.Constants
import com.example.thunder.util.UnitsConverters
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(var dailyList: MutableList<Daily>) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    lateinit var context: Context


    @SuppressLint("NotifyDataSetChanged")
    fun changeData(newList: List<Daily>) {
        dailyList.clear()
        dailyList.addAll(newList)
        notifyDataSetChanged()
    }

    class DailyViewHolder(var view: ItemDailyRecyclerBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding =
            ItemDailyRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return DailyViewHolder(binding)
    }

    override fun getItemCount(): Int = dailyList.size

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val item = dailyList[position]
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(dailyList[position].dt.toLong() * 1000)
        holder.view.dayNameTextView.text = localizingDays(
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
            holder.view.dayNameTextView.context
        )

        val unitConverters = UnitsConverters(context)

        holder.view.dayHighestTemperatureTextView.text =
            unitConverters.returnTemperatureUsingUserFormat(item.temp.max.toInt().toString())
        unitConverters.returnTemperatureUsingUserFormat(item.temp.max.toInt().toString())
        holder.view.dayLowestTemperatureTextView.text =
            unitConverters.returnTemperatureUsingUserFormat(item.temp.min.toInt().toString())
        Glide.with(holder.view.dayStatusImageView.context)
            .load(Constants.imageUrl + item.weather[0].icon + "@2x.png")
            .placeholder(R.drawable.home_fragment_main_circle)
            .into(holder.view.dayStatusImageView)
    }

    private fun localizingDays(day: String, context: Context): String {

        return when (day.trim()) {
            "Saturday" -> context.getString(R.string.saturday)
            "Sunday" -> context.getString(R.string.sunday)
            "Monday" -> context.getString(R.string.monday)
            "Tuesday" -> context.getString(R.string.tuesday)
            "Wednesday" -> context.getString(R.string.wednesday)
            "Friday" -> context.getString(R.string.friday)
            "Thursday" -> context.getString(R.string.thursday)
            else -> day
        }
    }

}