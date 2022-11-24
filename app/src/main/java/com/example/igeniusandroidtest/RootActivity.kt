package com.example.igeniusandroidtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.igeniusandroidtest.databinding.ActivityRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}