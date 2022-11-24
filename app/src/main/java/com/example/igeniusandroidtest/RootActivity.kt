package com.example.igeniusandroidtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.igeniusandroidtest.databinding.ActivityRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {
    private val binding: ActivityRootBinding by lazy { ActivityRootBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}