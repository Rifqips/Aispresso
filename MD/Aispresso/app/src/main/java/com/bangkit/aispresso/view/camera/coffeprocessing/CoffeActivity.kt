package com.bangkit.aispresso.view.camera.coffeprocessing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.aispresso.R
import com.bangkit.aispresso.databinding.ActivityCoffeBinding

class CoffeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCoffeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoffeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}