package com.bangkit.aispresso.view.dashboard.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.aispresso.data.model.faq.FaqSingleton
import com.bangkit.aispresso.data.storage.PreferencesClass
import com.bangkit.aispresso.databinding.FragmentHomeBinding
import com.bangkit.aispresso.view.adapter.faq.FaqAdapter
import com.bangkit.aispresso.view.counsultant.CounsultantActivity
import com.bangkit.aispresso.view.faq.FaqActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private lateinit var preferences: PreferencesClass
    private lateinit var mFirebaseInstance: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = PreferencesClass(requireActivity())
        mFirebaseInstance = FirebaseDatabase.getInstance().reference
        binding.tvUser.text = preferences.getValue("username")

        Glide.with(this)
            .load(preferences.getValue("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivUser)

        binding.tvViewAll.setOnClickListener {
            startActivity(Intent(context, FaqActivity::class.java))
        }

        binding.cvCounsultant.setOnClickListener {
            startActivity(Intent(context, CounsultantActivity::class.java))
        }
    }



}