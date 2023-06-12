package com.bangkit.aispresso.view.faq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.model.faq.FaqSingleton
import com.bangkit.aispresso.databinding.ActivityFaqBinding
import com.bangkit.aispresso.view.adapter.faq.FaqAdapter
import com.bangkit.aispresso.view.dashboard.DashboardActivity

class FaqActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerShown()

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    private fun recyclerShown(){
        binding.rvFaq.adapter = FaqAdapter(FaqSingleton.listProduk)
        binding.rvFaq.layoutManager = LinearLayoutManager(this)
    }
}