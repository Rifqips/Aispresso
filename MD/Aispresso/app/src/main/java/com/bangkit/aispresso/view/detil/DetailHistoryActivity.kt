package com.bangkit.aispresso.view.detil

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.aispresso.R
import com.bangkit.aispresso.databinding.ActivityDetailHistoryBinding
import com.bangkit.aispresso.view.dashboard.DashboardActivity

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getItem()

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    fun getItem(){
        val item = intent
        val gambar = item.getByteArrayExtra("gambar")

        val bitmap = gambar?.let { BitmapFactory.decodeByteArray(gambar, 0, it.size) }
        val title = item?.getStringExtra("title")
        val subtitle = item?.getStringExtra("subtitle")

        binding.ivUser.setImageBitmap(bitmap)
        binding.tvTitle.text = title
        binding.tvSubtitle.text = subtitle
    }

}