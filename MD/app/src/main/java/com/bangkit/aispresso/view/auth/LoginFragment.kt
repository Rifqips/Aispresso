package com.bangkit.aispresso.view.auth

import android.content.Intent
import com.bangkit.aispresso.R
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bangkit.aispresso.data.model.User
import com.bangkit.aispresso.data.storage.PreferencesClass
import com.bangkit.aispresso.databinding.FragmentLoginBinding
import com.bangkit.aispresso.view.dashboard.DashboardActivity
import com.bangkit.aispresso.view.onboarding.OnboardingActivity
import com.google.firebase.database.*

class LoginFragment : Fragment() {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mDatabase : DatabaseReference
    lateinit var preference : PreferencesClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDatabase = FirebaseDatabase.getInstance().getReference("user")
        preference = PreferencesClass(requireActivity())

        binding.btnAction.setOnClickListener{
            preference.setValue("email",binding.edEmail.text.toString())
            if ( binding.edEmail.equals("")){
                binding.edEmail.error = "Silakan tulis email Anda"
                binding.edEmail.requestFocus() // agar cursor fokus ke email
            }else if ( binding.edPassword.equals("")){
                binding.edPassword.error = "Silakan tulis password Anda"
                binding.edPassword.requestFocus() // agar cursor fokus ke email
            } else{
                pushLogin(binding.edEmail.text.toString(), binding.edPassword.text.toString())
            }
        }

        binding.btnRegister.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, RegisterFragment(), RegisterFragment::class.java.simpleName)
                /* shared element transition to main activity */
                addSharedElement(binding.labelAuth, "auth")
                addSharedElement(binding.edEmail, "email")
                addSharedElement(binding.edPassword, "password")
                addSharedElement(binding.containerMisc, "misc")
                commit()
            }
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null){
                    Toast.makeText(requireActivity(), "User tidak ditemukan", Toast.LENGTH_LONG).show()
                }
                else {
                    if(user.password.equals(iPassword)){

                        preference.setValue("user", user.username.toString())
                        preference.setValue("email", user.email.toString())
                        preference.setValue("status", "1")

                        val intent = Intent(requireActivity(), DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireActivity(), "Password Anda salah", Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireActivity(), databaseError.message, Toast.LENGTH_LONG).show()
            }
        })

    }


}