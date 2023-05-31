package com.bangkit.aispresso.view.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.aispresso.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}