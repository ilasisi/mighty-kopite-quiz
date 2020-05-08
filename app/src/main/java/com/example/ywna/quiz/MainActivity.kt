package com.example.ywna.quiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ywna.quiz.ui.quiz.IOnBackPressed
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    private var navView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_quiz -> hideBottomNav()
                R.id.navigation_prediction -> hideBottomNav()
                R.id.navigation_result -> hideBottomNav()
                R.id.navigation_login -> hideBottomNav()
                R.id.navigation_register -> hideBottomNav()
                R.id.navigation_reset_password -> hideBottomNav()
                else -> showBottomNav()
            }
        }
        navView?.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.quiz_container)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }

    private fun showBottomNav() {
        navView?.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        navView?.visibility = View.GONE
    }
}
