package com.bangkit.aispresso.view.admoob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.aispresso.R
import com.bangkit.aispresso.databinding.ActivityAdsBinding

class AdsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}