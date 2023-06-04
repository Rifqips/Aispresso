package com.bangkit.aispresso.view.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.model.history.coffe.ClassifyCoffeModel
import com.bangkit.aispresso.data.sqlite.ClassifyHelper
import com.bangkit.aispresso.data.utils.Helper.EXTRA_POSITION
import com.bangkit.aispresso.data.utils.Helper.EXTRA_REGISTRATION
import com.bangkit.aispresso.data.utils.Helper.REQUEST_ADD
import com.bangkit.aispresso.data.utils.Helper.REQUEST_UPDATE
import com.bangkit.aispresso.data.utils.Helper.RESULT_ADD
import com.bangkit.aispresso.data.utils.Helper.RESULT_DELETE
import com.bangkit.aispresso.data.utils.Helper.RESULT_UPDATE
import com.bangkit.aispresso.data.utils.Helper.mapCursorToArrayList
import com.bangkit.aispresso.databinding.ActivityHistoryBinding
import com.bangkit.aispresso.view.adapter.history.HistoryAdapter
import com.bangkit.aispresso.view.dashboard.DashboardActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var classifyHelper: ClassifyHelper
    private lateinit var adapter: HistoryAdapter
    private val EXTRA_STATE = "EXTRA_SATE"

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var coordinatorLayout: CoordinatorLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvRegister.layoutManager = LinearLayoutManager(this)
        binding.rvRegister.setHasFixedSize(true)
        adapter = HistoryAdapter(this)
        binding.rvRegister.adapter = adapter

        coordinatorLayout = binding.clDetail

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        handleViewOnEmpty()

        val biometricManager = BiometricManager.from(this)
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

        var executor: Executor = ContextCompat.getMainExecutor(this)

        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showSnackbarMessage(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    showSnackbarMessage(getString(R.string.success))
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

        classifyHelper = ClassifyHelper.getInstance(applicationContext)
        classifyHelper.open()
        if (savedInstanceState == null) {
            loadRegister()
        } else {
            val list = savedInstanceState.getParcelableArrayList<ClassifyCoffeModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listRegister = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listRegister)
    }

    private fun loadRegister() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressbar.visibility = View.VISIBLE
            val cursor = classifyHelper.queryAll()
            val register = mapCursorToArrayList(cursor)
            binding.progressbar.visibility = View.INVISIBLE

            if (register.size > 0) {
                adapter.listRegister = register
                handleViewOnEmpty()
            } else {
                adapter.listRegister = ArrayList()
                handleViewOnEmpty()
//                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    private fun handleViewOnEmpty() {
        if (adapter.listRegister.size > 0) {
            binding.tvEmpty.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.VISIBLE
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
                REQUEST_ADD -> if (resultCode == RESULT_ADD) {
                    val classify =
                        data.getParcelableExtra<ClassifyCoffeModel>(EXTRA_REGISTRATION) as ClassifyCoffeModel
                    adapter.addItem(classify)
                    binding.rvRegister.smoothScrollToPosition(adapter.itemCount - 1)
                    adapter.notifyDataSetChanged()
                    showSnackbarMessage("Satu item berhasil ditambahkan")
                    handleViewOnEmpty()

                }
                REQUEST_UPDATE -> when (resultCode) {
                    RESULT_UPDATE -> {
                        val regist =
                            data.getParcelableExtra<ClassifyCoffeModel>(EXTRA_REGISTRATION) as ClassifyCoffeModel
                        val position = data.getIntExtra(EXTRA_POSITION, 0)
                        adapter.updateItem(position, regist)
                        binding.rvRegister.smoothScrollToPosition(position)
                        adapter.notifyDataSetChanged()
                        showSnackbarMessage("Satu item berhasil diubah")
                        handleViewOnEmpty()
                    }
                    RESULT_DELETE -> {
                        val position = data.getIntExtra(EXTRA_POSITION, 0)
                        adapter.removeItem(position)
                        adapter.notifyDataSetChanged()
                        showSnackbarMessage("Satu item berhasil dihapus")
                        handleViewOnEmpty()
                    }

                }
            }
        }
    }

}