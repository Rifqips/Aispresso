package com.bangkit.aispresso.view.camera.leafprocessing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.aispresso.R
import com.bangkit.aispresso.databinding.ActivityLeafBinding

class LeafActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLeafBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeafBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}