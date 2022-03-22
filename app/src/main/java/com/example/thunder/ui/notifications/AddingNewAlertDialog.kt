package com.example.thunder.ui.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.thunder.R
import com.example.thunder.databinding.FragmentAddingNewAlertDialogBinding
import com.example.thunder.model.AlertModel
import com.example.thunder.util.DatePickerHelper
import com.example.thunder.util.TimePickerHelper
import com.example.thunder.util.notifier.AlertAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddingNewAlertDialog(

) : DialogFragment(R.layout.fragment_adding_new_alert_dialog) {

    private var _binding: FragmentAddingNewAlertDialogBinding? = null

    private val viewModel: AddingNewAlertDialogViewModel by viewModels()

    private val calendar = Calendar.getInstance()
    lateinit var alert: AlertModel


    @SuppressLint("SimpleDateFormat")
    private val formatDate = SimpleDateFormat("MMM dd")

    @SuppressLint("SimpleDateFormat")
    private val formatHour = SimpleDateFormat("hh:mm aa")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isStartDateSelected = false
    private var isEndDateSelected = false
    private var isTimeSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddingNewAlertDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        alert = AlertModel(0, 0, 0, 0, true)



        view.apply {
            binding.dateFromTextView.text = formatDate.format(calendar.time)
            binding.dateToTextView.text = formatDate.format(calendar.time)
            binding.chosenHourTextView.text = formatHour.format(calendar.time)

            binding.chooseFromConstraintLayout.setOnClickListener {
                DatePickerHelper(requireContext(),
                    object : DatePickerHelper.DateTimePickerInterface {
                        override fun onDateTimeSelected(calendar: Calendar) {
                            binding.dateFromTextView.text = formatDate.format(calendar.time)
                            alert.startDate = calendar.timeInMillis
                            isStartDateSelected = true
                            enableButtonWhenValid()
                        }

                    }).showDatePicker()

            }

            binding.chooseToConstraintLayout.setOnClickListener {
                DatePickerHelper(requireContext(),
                    object : DatePickerHelper.DateTimePickerInterface {
                        override fun onDateTimeSelected(calendar: Calendar) {
                            binding.dateToTextView.text = formatDate.format(calendar.time)
                            alert.endDate = calendar.timeInMillis
                            isEndDateSelected = true
                            enableButtonWhenValid()
                        }

                    }).showDatePicker()

            }

            binding.chooseTimeConstraintLayout.setOnClickListener {
                TimePickerHelper(requireContext(), object : TimePickerHelper.TimePickerInterface {
                    override fun onTimeSelected(calendar: Calendar) {
                        if (validation(calendar)) {
                            binding.chosenHourTextView.text = formatHour.format(calendar.time)
                            alert.fireAlertTime = calendar.timeInMillis
                            isTimeSelected = true
                            enableButtonWhenValid()
                        }
                    }

                }).showPicker()
            }



            binding.saveButton.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {

                    alert.id = viewModel.insertAlert(alert)
                }.invokeOnCompletion {
                    var alertAlarm = AlertAlarmManager(context, alert)
                }
                it.isEnabled = false
                dialog?.dismiss()

            }
        }
        return root
    }

    private fun validation(selectedTime: Calendar): Boolean {
        val result: Boolean = if (System.currentTimeMillis() + 59 * 1000 > selectedTime.time.time) {
            Toast.makeText(context, "time should be at least 1 min from now", Toast.LENGTH_SHORT)
                .show()
            false
        } else {
            true
        }
        return result
    }

    private fun enableButtonWhenValid() {
        binding.saveButton.isEnabled = isStartDateSelected && isEndDateSelected && isTimeSelected
        binding.saveButton.isClickable = isStartDateSelected && isEndDateSelected && isTimeSelected
    }

}