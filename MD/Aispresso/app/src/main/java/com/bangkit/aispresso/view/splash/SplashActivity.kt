package com.bangkit.aispresso.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bangkit.aispresso.data.storage.PreferencesClass
import com.bangkit.aispresso.databinding.ActivitySplashBinding
import com.bangkit.aispresso.view.dashboard.DashboardActivity
import com.bangkit.aispresso.view.onboarding.OnboardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding
    private lateinit var preferences: PreferencesClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        preferences = PreferencesClass(this)

        Handler(Looper.myLooper()!!).postDelayed({
            if (preferences.sharedPref.getString("email","") == "" && preferences.sharedPref.getString("password", "") == "") {
                val intentLogin = Intent(this, OnboardingActivity::class.java)
                startActivity(intentLogin)
            } else {
                val intent = Intent(this,DashboardActivity::class.java)
                intent.putExtra("email", preferences.sharedPref.getString("email",""))
                startActivity(intent)
            }
        },3000)
        setContentView(binding.root)

    }
}