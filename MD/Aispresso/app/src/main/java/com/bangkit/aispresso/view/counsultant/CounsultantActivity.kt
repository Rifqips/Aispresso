package com.bangkit.aispresso.view.counsultant

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.aispresso.databinding.ActivityCounsultantBinding
import com.bangkit.aispresso.view.adapter.counsultant.CounstultantAdapter
import com.bangkit.aispresso.view.dashboard.DashboardActivity
import com.bangkit.aispresso.viewmodel.ViewModelCounsultant

class CounsultantActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCounsultantBinding
    lateinit var adapter: CounstultantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounsultantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVmAdapter()
        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun setVmAdapter(){
        val viewModel = ViewModelProvider(this)[ViewModelCounsultant::class.java]
        viewModel.callApiCounsultant()
        viewModel.liveDataCounsultant.observe(this) {
            adapter = CounstultantAdapter(it.data)
            if (it != null)
            binding.homeProgressBar.visibility = View.GONE
            binding.rvCounsultant.layoutManager = LinearLayoutManager(this)
            binding.rvCounsultant.adapter = CounstultantAdapter(it.data)
            if(it == null)
            binding.homeProgressBar.visibility = View.VISIBLE
        }
    }
}