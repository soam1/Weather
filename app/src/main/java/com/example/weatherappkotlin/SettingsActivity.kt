package com.example.weatherappkotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var sharedPref: SharedPref

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            sharedPref = SharedPref(requireContext())

            // Find the EditTextPreference for min_temperature
//            val minTempPreference = findPreference<EditTextPreference>("min_temperature")
//            minTempPreference?.setOnBindEditTextListener { editText ->
//                // Set the input type for the EditText
//                editText.inputType =
//                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
//            }
//            //similarly for max_temperature
//            val maxTempPreference = findPreference<EditTextPreference>("max_temperature")
//            maxTempPreference?.setOnBindEditTextListener { editText ->
//                editText.inputType =
//                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
//            }

            findPreference<ListPreference>("update_interval")?.setOnPreferenceChangeListener { _, newValue ->
                val updateTime = when (newValue) {
                    "5" -> 5 * 60 * 1000L // Every 5 minutes
                    "10" -> 10 * 60 * 1000L // Every 10 minutes
                    "30" -> 30 * 60 * 1000L // Every 30 minutes
                    "60" -> 60 * 60 * 1000L // Every 60 minutes
                    else -> 0
                }
                Toast.makeText(
                    context,
                    "Please restart the app to apply changes",
                    Toast.LENGTH_SHORT
                ).show()
                sharedPref.setUpdateTime(updateTime)
                true
            }

            findPreference<ListPreference>("unit_system")?.setOnPreferenceChangeListener { _, newValue ->
                sharedPref.setUnit(newValue.toString())
                Toast.makeText(
                    context,
                    "Please restart the app to apply changes",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
            findPreference<EditTextPreference>("min_temperature")?.setOnPreferenceChangeListener { _, newValue ->
                val minTemperature = newValue.toString().toDoubleOrNull()
                if (minTemperature != null) {
                    sharedPref.saveThresholdMinTemp(minTemperature)
                    Toast.makeText(
                        context,
                        "Minimum temperature threshold saved",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                } else {
                    Toast.makeText(context, "Invalid minimum temperature value", Toast.LENGTH_SHORT)
                        .show()
                    false
                }
            }

            findPreference<EditTextPreference>("max_temperature")?.setOnPreferenceChangeListener { _, newValue ->
                val maxTemperature = newValue.toString().toDoubleOrNull()
                if (maxTemperature != null) {
                    sharedPref.saveThresholdMaxTemp(maxTemperature)
                    Toast.makeText(
                        context,
                        "Maximum temperature threshold saved",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                } else {
                    Toast.makeText(context, "Invalid maximum temperature value", Toast.LENGTH_SHORT)
                        .show()
                    false
                }
            }

        }

    }
}