package com.example.dreamchaser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_pomodoro.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.fragmentContainerView)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.setupWithNavController(navController)
    }

    /**
     * disable Bottom Navigation when call the function
     */
    fun disableBottomNav(){
        findViewById<BottomNavigationView>(R.id.bottomNav).menu.forEach { it.isEnabled = false }
    }

    /**
     * enable Bottom Navigation when call the function
     */
    fun enableBottomNav(){
        findViewById<BottomNavigationView>(R.id.bottomNav).menu.forEach { it.isEnabled = true }
    }
}




























