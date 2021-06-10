package com.fmg.mygrainchainroute.view

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fmg.mygrainchainroute.AbstractActivity
import com.fmg.mygrainchainroute.Constants
import com.fmg.mygrainchainroute.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AbstractActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView;
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateStartRouteFragmentIfNeeded(intent)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navController = findNavController(R.id.nav_host_main)
        bottomNavigationView.setupWithNavController(navController)

    }

    private fun navigateStartRouteFragmentIfNeeded(intent: Intent){
        if(intent.action == Constants.ACTION_SHOW_ROUTE_TRACKING_FRAGMENT){
            navController.navigate(R.id.startRouteFragment)
        }
    }
}
