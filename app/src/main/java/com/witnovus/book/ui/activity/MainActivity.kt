package com.witnovus.book.ui.activity

import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.witnovus.book.R
import com.witnovus.book.databinding.ActivityMainBinding

/**
 * This is Launcher Activity and manages the navigation graph
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

     lateinit  var binding: ActivityMainBinding
     lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // setUp NavhostFragment for fragments
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment!!.navController
    }
}