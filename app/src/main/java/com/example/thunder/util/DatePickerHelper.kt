package com.example.thunder.util

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.*


class DatePickerHelper(var context: Context, var iface: DateTimePickerInterface?) :

    DatePickerDialog.OnDateSetListener {

    val selectedDate = Calendar.getInstance()
    fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            context,
            this,
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = selectedDate.timeInMillis
        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.DAY_OF_MONTH, day)
        iface?.onDateTimeSelected(selectedDate)
    }


    interface DateTimePickerInterface {
        fun onDateTimeSelected(calendar: Calendar)
    }

}