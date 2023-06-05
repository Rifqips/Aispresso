package com.bangkit.aispresso.view.dashboard.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.model.history.coffe.ClassifyCoffeModel
import com.bangkit.aispresso.data.sqlite.ClassifyHelper
import com.bangkit.aispresso.data.utils.Helper
import com.bangkit.aispresso.databinding.FragmentHistoryBinding
import com.bangkit.aispresso.view.adapter.history.HistoryAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private lateinit var classifyHelper: ClassifyHelper
    private lateinit var adapter: HistoryAdapter
    private val EXTRA_STATE = "EXTRA_SATE"

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var coordinatorLayout: CoordinatorLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRegister.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRegister.setHasFixedSize(true)
        adapter = HistoryAdapter()
        binding.rvRegister.adapter = adapter

        coordinatorLayout = binding.clDetail

        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                showSnackbarMessage(getString(R.string.error_no_hardware))
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                showSnackbarMessage(getString(R.string.error_hw_unavailable))
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showSnackbarMessage(getString(R.string.error_none_enrolled))
            }
        }

        var executor: Executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showSnackbarMessage(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(context,R.string.success, Toast.LENGTH_SHORT ).show()
                    coordinatorLayout!!.visibility = View.VISIBLE
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.title_biometric_prompt))
            .setDescription(getString(R.string.description_biometric_prompt))
            .setDeviceCredentialAllowed(true).build()

        biometricPrompt.authenticate(promptInfo)

        classifyHelper = ClassifyHelper.getInstance(requireContext())
        classifyHelper.open()
        if (savedInstanceState == null) {
            loadRegister()
        } else {
            val list = savedInstanceState.getParcelableArrayList<ClassifyCoffeModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listHistory = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listHistory)
    }

    private fun loadRegister() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressbar.visibility = View.VISIBLE
            val cursor = classifyHelper.queryAll()
            val register = Helper.mapCursorToArrayList(cursor)
            binding.progressbar.visibility = View.INVISIBLE

            if (register.size > 0) {
                adapter.listHistory = register
            } else {
                adapter.listHistory = ArrayList()
//                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvRegister, message, Snackbar.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                Helper.REQUEST_ADD -> if (resultCode == Helper.RESULT_ADD) {
                    val classify =
                        data.getParcelableExtra<ClassifyCoffeModel>(Helper.EXTRA_REGISTRATION) as ClassifyCoffeModel
                    adapter.addItem(classify)
                    binding.rvRegister.smoothScrollToPosition(adapter.itemCount - 1)
                    adapter.notifyDataSetChanged()
                    showSnackbarMessage("Satu item berhasil ditambahkan")

                }
                Helper.REQUEST_UPDATE -> when (resultCode) {
                    Helper.RESULT_UPDATE -> {
                        val regist =
                            data.getParcelableExtra<ClassifyCoffeModel>(Helper.EXTRA_REGISTRATION) as ClassifyCoffeModel
                        val position = data.getIntExtra(Helper.EXTRA_POSITION, 0)
                        adapter.updateItem(position, regist)
                        binding.rvRegister.smoothScrollToPosition(position)
                        adapter.notifyDataSetChanged()
                        showSnackbarMessage("Satu item berhasil diubah")
                    }
                    Helper.RESULT_DELETE -> {
                        val position = data.getIntExtra(Helper.EXTRA_POSITION, 0)
                        adapter.removeItem(position)
                        adapter.notifyDataSetChanged()
                        showSnackbarMessage("Satu item berhasil dihapus")
                    }

                }
            }
        }
    }

}