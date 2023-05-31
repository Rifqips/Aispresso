package com.bangkit.aispresso.view.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bangkit.aispresso.R
import com.bangkit.aispresso.databinding.ActivityDashboardBinding
import com.bangkit.aispresso.view.camera.CameraActivity
import com.bangkit.aispresso.view.dashboard.history.HistoryFragment
import com.bangkit.aispresso.view.dashboard.home.HomeFragment
import com.bangkit.aispresso.view.dashboard.notification.NotificationFragment
import com.bangkit.aispresso.view.dashboard.profile.ProfileFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val fragmentHome = HomeFragment()
    private val fragmentHistory = HistoryFragment()
    private val fragmentNotification = NotificationFragment()
    private val fragmentProfile = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNav.background = null
        loadFragment(fragmentHome)

        binding.fab.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, CameraActivity::class.java))
        }

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome ->
                    loadFragment(fragmentHome)
                R.id.menuHistory ->
                    loadFragment(fragmentHistory)
                R.id.menuNotification ->
                    loadFragment(fragmentNotification)
                R.id.menuProfile ->
                    loadFragment(fragmentProfile)
            }
            true
        }

    }

    private  fun loadFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_dashboard, fragment)
            .commit()
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}