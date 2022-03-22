package com.example.thunder.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.thunder.R
import com.example.thunder.databinding.FragmentSettings2Binding
import com.example.thunder.util.SharedPreferencesFactory
import java.util.*

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private var _binding: FragmentSettings2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettings2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferencesFactory = SharedPreferencesFactory(requireContext())

        when {
            sharedPreferencesFactory.getUnitsOfTemperature().equals("K") -> {
                binding.kRadioButton.isChecked = true
            }
            sharedPreferencesFactory.getUnitsOfTemperature().equals("C") -> {
                binding.cRadioButton.isChecked = true
            }
            sharedPreferencesFactory.getUnitsOfTemperature().equals("F") -> {
                binding.fRadioButton.isChecked = true
            }
        }

        when {
            sharedPreferencesFactory.getNotificationEnabledOrDisabled() -> {
                binding.enableNotificationRadioButton.isChecked = true
            }
            !sharedPreferencesFactory.getNotificationEnabledOrDisabled() -> {
                binding.disableNotificationRadioButton.isChecked = true
            }
        }

        if (sharedPreferencesFactory.getAlertEnabledOrDisabled()) {
            binding.enableAlertRadioButton.isChecked = true
        } else {
            binding.disableAlertRadioButton.isChecked = true
        }

        val lang = Locale.getDefault().language
        if (lang.equals("en")) {
            binding.englishRadioButton.isChecked = true
        } else if (lang.equals("ar")) {
            binding.arabicRadioButton.isChecked = true
        }

        when {
            sharedPreferencesFactory.getUnitsOfWindSpeed().equals("meter/sec") -> {
                binding.meterPerSecondRadioButton.isChecked = true
            }
            sharedPreferencesFactory.getUnitsOfWindSpeed().equals("miles/hour") -> {
                binding.milePerHourRadioButton.isChecked = true
            }
        }



        binding.languageRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.arabicRadioButton) {
                viewModel.saveLanguageInSharedPreferences("ar", requireContext())
                viewModel.updateApplicationLanguage("ar", requireActivity())
            } else if (i == R.id.englishRadioButton) {
                viewModel.saveLanguageInSharedPreferences("en", requireContext())
                viewModel.updateApplicationLanguage("en", requireActivity())
            }
        }

        binding.temperatureRadioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.kRadioButton -> {
                    viewModel.saveTemperatureUnitInSharedPreferences("K", requireContext())
                }
                R.id.cRadioButton -> {
                    viewModel.saveTemperatureUnitInSharedPreferences("C", requireContext())
                }
                R.id.fRadioButton -> {
                    viewModel.saveTemperatureUnitInSharedPreferences("F", requireContext())
                }
            }
        }

        binding.notificationsRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.enableNotificationRadioButton) {
                viewModel.saveNotificationEnabledOrDisabledInSharedPreferences(
                    true,
                    requireContext()
                )
            } else if (i == R.id.disableNotificationRadioButton) {
                viewModel.saveNotificationEnabledOrDisabledInSharedPreferences(
                    false,
                    requireContext()
                )
            }
        }

        binding.windSpeedRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.milePerHourRadioButton) {
                viewModel.saveWindSpeedUnitInSharedPreferences("miles/hour", requireContext())
            } else if (i == R.id.meterPerSecondRadioButton) {
                viewModel.saveWindSpeedUnitInSharedPreferences("meter/sec", requireContext())
            }
        }

        binding.alertRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.enableAlertRadioButton) {
                viewModel.saveAlertEnabledOrDisabledInSharedPreferences(true, requireContext())
            } else if (i == R.id.disableAlertRadioButton) {
                viewModel.saveAlertEnabledOrDisabledInSharedPreferences(false, requireContext())
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}