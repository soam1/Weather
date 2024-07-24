package com.example.weatherappkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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


            findPreference<ListPreference>("update_interval")?.setOnPreferenceChangeListener { _, newValue ->
                val updateTime = when (newValue) {
                    "5" -> 5 * 60 * 1000L // Every 5 minutes
                    "10" -> 10 * 60 * 1000L // Every 10 minutes
                    "30" -> 30 * 60 * 1000L // Every 30 minutes
                    "60" -> 60 * 60 * 1000L // Every 60 minutes
                    else -> 0
                }
                sharedPref.setUpdateTime(updateTime)
                true
            }

            findPreference<ListPreference>("unit_system")?.setOnPreferenceChangeListener { _, newValue ->
                sharedPref.setUnit(newValue.toString())
                true
            }
        }

    }
}