package com.bangkit.aispresso.view.dashboard.profile

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.storage.PreferencesClass
import com.bangkit.aispresso.data.utils.Constanta
import com.bangkit.aispresso.databinding.FragmentProfileBinding
import com.bangkit.aispresso.view.auth.AuthActivity
import com.bangkit.aispresso.view.camera.coffeprocessing.CoffeClasifyActivity
import com.bangkit.aispresso.view.camera.leafprocessing.LeafActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    private lateinit var preferences: PreferencesClass
    private lateinit var mFirebaseInstance: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = PreferencesClass(requireActivity())
        mFirebaseInstance = FirebaseDatabase.getInstance().reference
        binding.tvNamaUser.text = preferences.getValue("username")

        Glide.with(this)
            .load(preferences.getValue("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivSetImage)

        binding.btnLogout.setOnClickListener {

            val dialog = Constanta.dialogLogout(it.context, getString(R.string.logout_permession), Gravity.CENTER)
            val btnCoffe = dialog.findViewById<Button>(R.id.button_yes)
            val btnLeaf = dialog.findViewById<Button>(R.id.button_no)
            btnCoffe.setOnClickListener {
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                dialog.dismiss()
            }
            btnLeaf.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}