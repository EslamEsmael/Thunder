package com.example.thunder

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.Window
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.thunder.databinding.ActivityMainBinding
import com.example.thunder.util.ContextUtils
import com.example.thunder.util.SharedPreferencesFactory
import com.example.thunder.util.checkOverlayPermission
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("RestrictedApi")
    override fun attachBaseContext(newBase: Context) {
        // get chosen language from shread preference
        val shared = SharedPreferencesFactory(newBase)
        if (shared.getIsLangSet()) {
            val localeToSwitchTo = shared.getLanguage()
            val localeUpdatedContext: ContextWrapper =
                ContextUtils.updateLocale(newBase, Locale(localeToSwitchTo))
            super.attachBaseContext(localeUpdatedContext)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()

        checkOverlayPermission(this@MainActivity)

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

    }
}