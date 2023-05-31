package com.bangkit.aispresso.view.dashboard.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.aispresso.R
import com.bangkit.aispresso.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private lateinit var binding : FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }
}