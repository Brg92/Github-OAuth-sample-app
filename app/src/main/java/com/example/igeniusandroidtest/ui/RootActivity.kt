package com.example.igeniusandroidtest.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.igeniusandroidtest.R
import com.example.igeniusandroidtest.databinding.ActivityRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController =
            (supportFragmentManager.findFragmentById(R.id.root_host_fragment) as NavHostFragment).navController

        onBackPressedDispatcher.addCallback(this) {
            if (navController.backQueue.size > 4) {
                navController.navigateUp()
            } else {
                return@addCallback
            }
        }

    }
}