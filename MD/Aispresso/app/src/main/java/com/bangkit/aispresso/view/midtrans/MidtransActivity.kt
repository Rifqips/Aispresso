package com.bangkit.aispresso.view.midtrans

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bangkit.aispresso.R
import com.bangkit.aispresso.databinding.ActivityMidtransBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants

class MidtransActivity : AppCompatActivity() {


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
//                    Toast.makeText(this, "${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
            if (transactionResult != null) {
                when (transactionResult.status) {
                    UiKitConstants.STATUS_SUCCESS -> {
                        Toast.makeText(this, "Transaction Finished. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    UiKitConstants.STATUS_PENDING -> {
                        Toast.makeText(this, "Transaction Pending. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    UiKitConstants.STATUS_FAILED -> {
                        Toast.makeText(this, "Transaction Failed. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    UiKitConstants.STATUS_CANCELED -> {
                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show()
                    }
                    UiKitConstants.STATUS_INVALID -> {
                        Toast.makeText(this, "Transaction Invalid. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this, "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private var oneTime = 100000
    private var twoTimes = 200000
    private var threeTimes = 300000
    private var totalPayment = ""
    private var tp = ""
    private var token = "4221f03a-2dac-408c-aac3-98e0a27387b7"

    private lateinit var binding : ActivityMidtransBinding

    @SuppressLint("MissingInflatedId", "SetTextI18n", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMidtransBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manipulateConsultationItems()
        manipulateClassifyItems()
        counsultantPayment()
        classifyPayment()

        binding.btnAction.setOnClickListener {
            buildUiKit()
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val btnPay = view.findViewById<Button>(R.id.idBtnPay)
            val btnDismis = view.findViewById<Button>(R.id.idBtnDismiss)
            val textTotal = view.findViewById<TextView>(R.id.tvTotalPayment)
            val totalAfterTax = tp.toInt() + 4000
            textTotal.text = "Rp.$totalAfterTax"
            btnPay.setOnClickListener {
                UiKitApi.getDefaultInstance().startPaymentUiFlow(
                    activity = this@MidtransActivity,
                    launcher = launcher,
                    // mengambil token
                    snapToken = token
                )
            }
            btnDismis.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }

    }

    private fun manipulateConsultationItems(){
        binding.ivAdd.setOnClickListener {
            binding.rgChoosenPayment.isGone = false
            binding.ivClear.isVisible = true
            binding.ivAdd.isVisible = false
        }
        binding.ivClear.setOnClickListener {
            binding.rgChoosenPayment.isGone = true
            binding.ivClear.isVisible = false
            binding.ivAdd.isVisible = true
        }
    }

    private fun manipulateClassifyItems(){
        binding.ivAddClassify.setOnClickListener {
            binding.rgChoosenPaymentClassify.isGone = false
            binding.ivClearClassify.isVisible = true
            binding.ivAddClassify.isVisible = false
        }
        binding.ivClearClassify.setOnClickListener {
            binding.rgChoosenPaymentClassify.isGone = true
            binding.ivClearClassify.isVisible = false
            binding.ivAddClassify.isVisible = true
        }
    }

    private fun counsultantPayment(){
        binding.rgChoosenPayment.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (binding.rbOneTime.isChecked){
                val i = oneTime.toString()
                totalPayment = i
                tp = totalPayment
            }
            if (binding.rbTwoTimes.isChecked){
                val i = twoTimes.toString()
                totalPayment = i
                tp = totalPayment
            }
            if (binding.rbThreeTimes.isChecked){
                val i = threeTimes.toString()
                totalPayment = i
                tp = totalPayment
            }
        }
    }

    private fun classifyPayment(){
        binding.rgChoosenPaymentClassify.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (binding.rbOneTimeClassify.isChecked){
                val i = oneTime.toString()
                totalPayment = i
                tp = totalPayment
            }
            if (binding.rbTwoTimesClassify.isChecked){
                val i = twoTimes.toString()
                totalPayment = i
                tp = totalPayment
            }
            if (binding.rbThreeTimesClassify.isChecked){
                val i = threeTimes.toString()
                totalPayment = i
                tp = totalPayment
            }
        }
    }

    private fun buildUiKit() {
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl("https://mufti.my.id/snap-api/")
            .withMerchantClientKey("SB-Mid-client-tbFBWIwqhPl3miNr")
            .enableLog(true)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UiKitApi.getDefaultInstance().uiKitSetting
        uIKitCustomSetting.saveCardChecked = true
    }
}