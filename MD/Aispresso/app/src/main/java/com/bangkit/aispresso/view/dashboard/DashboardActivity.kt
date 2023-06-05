package com.bangkit.aispresso.view.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.utils.Constanta
import com.bangkit.aispresso.databinding.ActivityDashboardBinding
import com.bangkit.aispresso.view.camera.coffeprocessing.CoffeClasifyActivity
import com.bangkit.aispresso.view.camera.leafprocessing.LeafActivity
import com.bangkit.aispresso.view.dashboard.history.HistoryFragment
import com.bangkit.aispresso.view.dashboard.home.HomeFragment
import com.bangkit.aispresso.view.dashboard.notification.NotificationFragment
import com.bangkit.aispresso.view.dashboard.profile.ProfileFragment
import com.bangkit.aispresso.view.dashboard.wheaterfragment.WheaterFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val fragmentHome = HomeFragment()
    private val fragmentWheater = WheaterFragment()
    private val fragmentHistory = HistoryFragment()
    private val fragmentProfile = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNav.background = null
        loadFragment(fragmentHome)

        binding.fab.setOnClickListener {
            val dialog = Constanta.dialogInfoOption(this, getString(R.string.UI_info_options_camera), Gravity.CENTER)
            val btnCoffe = dialog.findViewById<Button>(R.id.button_kopi)
            val btnLeaf = dialog.findViewById<Button>(R.id.button_daun)
            val ivBack = dialog.findViewById<ImageView>(R.id.iv_back)
            btnCoffe.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, CoffeClasifyActivity::class.java))
                dialog.dismiss()
            }
            btnLeaf.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, LeafActivity::class.java))
                dialog.dismiss()
            }
            ivBack.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome ->
                    loadFragment(fragmentHome)
                R.id.menuWheater ->
                    loadFragment(fragmentWheater)
                R.id.menuHistory ->
                    loadFragment(fragmentHistory)
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