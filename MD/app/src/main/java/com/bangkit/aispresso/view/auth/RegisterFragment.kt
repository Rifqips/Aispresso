package com.bangkit.aispresso.view.auth

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.storage.PreferencesClass
import com.bangkit.aispresso.databinding.FragmentReigisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentReigisterBinding

    lateinit var preferences : PreferencesClass

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
        binding = FragmentReigisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        preferences = PreferencesClass(requireContext())

        binding.btnLogin.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, LoginFragment(), LoginFragment::class.java.simpleName)
                /* shared element transition to main activity */
                addSharedElement(binding.labelAuth, "auth")
                addSharedElement(binding.edEmail, "email")
                addSharedElement(binding.edPassword, "password")
                addSharedElement(binding.containerMisc, "misc")
                commit()
            }
        }

        binding.btnAction.setOnClickListener {
            preferences.setValue("username", binding.edName.text.toString())
            preferences.setValue("telepon", binding.edPhone.text.toString())
            preferences.setValue("email", binding.edEmail.text.toString())
            preferences.setValue("password", binding.edPassword.text.toString())

            if ( binding.edName.equals("")){
                binding.edName.error = "Silakan isi username Anda"
                binding.edName.requestFocus()
            } else if (binding.edPhone.equals("")){
                binding.edPhone.error = "Silakan isi no telepon Anda"
                binding.edPhone.requestFocus()
            } else if (binding.edPassword.equals("")){
                binding.edPassword.error = "Silakan isi password Anda"
                binding.edPassword.requestFocus()
            }else if (binding.edEmail.equals("")){
                binding.edEmail.error = "Silakan isi nama Anda"
                binding.edEmail.requestFocus()
            } else {
                val statusUsername = binding.edName.text!!.indexOf(".")
                if (statusUsername >=0) {
                    binding.edName.error = "Silahkan tulis Username Anda tanpa ."
                    binding.edName.requestFocus()
                }
            }

            startActivity(Intent(requireActivity(),UploadActivity::class.java ))

        }

    }

}