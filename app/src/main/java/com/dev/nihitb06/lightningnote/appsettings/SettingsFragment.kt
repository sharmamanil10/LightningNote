package com.dev.nihitb06.lightningnote.appsettings

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager

import com.dev.nihitb06.lightningnote.R

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity.title = getString(R.string.settings)

        addPreferencesFromResource(R.xml.settings)

        PreferenceManager.getDefaultSharedPreferences(activity).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        activity.title = getString(R.string.app_name)

        PreferenceManager.getDefaultSharedPreferences(activity).unregisterOnSharedPreferenceChangeListener(this)

        super.onDestroy()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, key: String?) {
        when(key) {
            getString(R.string.key_theme) -> restart()
        }
    }

    private fun restart() {
        startActivity(activity.intent)
        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)

        activity.finish()
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
