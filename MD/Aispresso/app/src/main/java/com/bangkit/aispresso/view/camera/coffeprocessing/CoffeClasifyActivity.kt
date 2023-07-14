package com.bangkit.aispresso.view.camera.coffeprocessing

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.model.history.coffe.ClassifyCoffeModel
import com.bangkit.aispresso.data.sqlite.ClassifyHelper
import com.bangkit.aispresso.data.sqlite.ClassifybaseRegsiter
import com.bangkit.aispresso.data.utils.Constanta
import com.bangkit.aispresso.data.utils.Helper.ALERT_DIALOG_CLOSE
import com.bangkit.aispresso.data.utils.Helper.ALERT_DIALOG_DELETE
import com.bangkit.aispresso.data.utils.Helper.EXTRA_POSITION
import com.bangkit.aispresso.data.utils.Helper.EXTRA_REGISTRATION
import com.bangkit.aispresso.data.utils.Helper.RESULT_ADD
import com.bangkit.aispresso.data.utils.Helper.RESULT_DELETE
import com.bangkit.aispresso.data.utils.Helper.RESULT_UPDATE
import com.bangkit.aispresso.databinding.ActivityCoffeClasifyBinding
import com.bangkit.aispresso.ml.Mlkopi
import com.bangkit.aispresso.view.admoob.AdsActivity
import com.bangkit.aispresso.view.camera.leafprocessing.LeafActivity
import com.bangkit.aispresso.view.dashboard.DashboardActivity
import com.bangkit.aispresso.view.midtrans.MidtransActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class CoffeClasifyActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityCoffeClasifyBinding
    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardedAd: RewardedAd? = null

    var imageSize = 224

    private lateinit var classifyHelper: ClassifyHelper
    private var imageView: ImageView? = null

    private val RESULT_LOAD_IMAGE = 123
    private val REQUEST_CODE_GALLERY = 999


    private var isEdit = false
    private var classify: ClassifyCoffeModel? = null
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoffeClasifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.bannerID.loadAd(adRequest) // Banner load

        classifyHelper = ClassifyHelper.getInstance(applicationContext)
        classifyHelper.open()

        classify = intent.getParcelableExtra(EXTRA_REGISTRATION)
        if (classify != null){
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            classify = ClassifyCoffeModel()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit){
            actionBarTitle = "Ubah"
            btnTitle = "Update"
            classify?.let { it ->
                binding.result.text = it.classified
                binding.confidence.text = it.considers

                val registerImage: ByteArray? = it.image
                val bitmap =
                    registerImage?.let { it1 -> BitmapFactory.decodeByteArray(registerImage, 0, it1.size) }
                binding.imageView.setImageBitmap(bitmap)
            }!!
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "save"
        }


        actionBar?.title = actionBarTitle
        binding.save.text = btnTitle
        binding.save.setOnClickListener(this)

        imageView = binding.imageView

        imageView!!.setOnClickListener {

            ActivityCompat.requestPermissions(
                this@CoffeClasifyActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE

                ),
                REQUEST_CODE_GALLERY
            )
        }

        binding.result.setOnEditorActionListener { v, actionId, event ->
            classify?.let { it ->
                it.classified = v.text.toString()
            }
            false
        }

        binding.confidence.setOnEditorActionListener { v, actionId, event ->
            classify?.let { it ->
                it.considers = v.text.toString()
            }
            false
        }

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.button.setOnClickListener{
            val dialog = Constanta.dialogCameraOption(this, getString(R.string.UI_info_options_camera), Gravity.CENTER)
            val btnAds = dialog.findViewById<CardView>(R.id.cv_ads)
            val btnPremium = dialog.findViewById<CardView>(R.id.cv_premium)
            val ivBack = dialog.findViewById<ImageView>(R.id.iv_back)
            btnAds.setOnClickListener {
                RewardedAd.load(this,
                    CoffeClasifyActivity.REWARDED_AD,
                    adRequest,
                    object : RewardedAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            showMessage(adError.message)
                            mRewardedAd = null
                            Intent(this@CoffeClasifyActivity, AdsActivity::class.java).apply {
                                startActivity(this)
                            }
                        }

                        override fun onAdLoaded(rewardedAd: RewardedAd) {
                            showMessage(getString(R.string.ad_info))
                            mRewardedAd = rewardedAd
                            mRewardedAd?.show(this@CoffeClasifyActivity) { rewardItem ->
                                val rewardAmount = rewardItem.amount
                                showMessage(getString(R.string.rewarded_info) + " " + rewardAmount)
                            }
                        }
                    })
                InterstitialAd.load(this,
                    CoffeClasifyActivity.INTERSTITIAL_AD,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            showMessage(adError.message)
                            mInterstitialAd = null
                            Intent(this@CoffeClasifyActivity, AdsActivity::class.java).apply {
                                startActivity(this)
                            }
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            showMessage(getString(R.string.ad_info))
                            mInterstitialAd = interstitialAd
                            mInterstitialAd?.show(this@CoffeClasifyActivity)

                            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(cameraIntent, 1)
                            } else {
                                //Request camera permission if we don't have it.
                                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
                            }
                        }
                    })
                dialog.dismiss()
            }
            btnPremium.setOnClickListener {
                startActivity(Intent(this@CoffeClasifyActivity, MidtransActivity::class.java))
                dialog.dismiss()
            }
            ivBack.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }


    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Register"
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = classifyHelper.deleteById(classify?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@CoffeClasifyActivity,
                            "Gagal menghapus data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    fun classifyImage(image: Bitmap?) {
        try {
            val model = Mlkopi.newInstance(applicationContext)

            // Creates inputs for reference.
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            // get 1D array of 224 * 224 pixels in image
            val intValues = IntArray(imageSize * imageSize)
            image!!.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val bytes = intValues[pixel++] // RGB
                    byteBuffer.putFloat((bytes shr 16 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((bytes shr 8 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((bytes and 0xFF) * (1f / 255f))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs: Mlkopi.Outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf("Premium", "Longberry", "Peaberry", "Defect")
            binding.result.text = classes[maxPos]
            var s = ""
            for (i in classes.indices) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
            }
            binding.confidence.text = s

            binding.save.isVisible = true
            binding.invisibleSave.isInvisible = true
            expertSystem()

            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    private fun expertSystem(){
        val textResult = binding.result.text


        if (textResult == "Peaberry" ){
            val dialog = Constanta.dialogExpertSystem(this, getString(R.string.Peaberry), Gravity.CENTER)
            val ivBack = dialog.findViewById<ImageView>(R.id.iv_back)
            ivBack.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        if (textResult == "Longberry" ){
            val dialog = Constanta.dialogExpertSystem(this, getString(R.string.Longberry), Gravity.CENTER)
            val ivBack = dialog.findViewById<ImageView>(R.id.iv_back)
            ivBack.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        if (textResult == "Premium" ){
            val dialog = Constanta.dialogExpertSystem(this, getString(R.string.Premium), Gravity.CENTER)
            val ivBack = dialog.findViewById<ImageView>(R.id.iv_back)
            ivBack.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        if (textResult == "Defect" ){
            val dialog = Constanta.dialogExpertSystem(this, getString(R.string.Defect), Gravity.CENTER)
            val ivBack = dialog.findViewById<ImageView>(R.id.iv_back)
            ivBack.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            var image = data!!.extras!!["data"] as Bitmap?
            val dimension = image!!.width.coerceAtMost(image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            binding.imageView.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
            classifyImage(image)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun imageViewToByte(image: ImageView): ByteArray? {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
    override fun onClick(v: View?) {

        if(v?.id == R.id.save) {

            val classified = binding.result.text.toString().trim()
            val consider = binding.confidence.text.toString().trim()

            classify?.classified = classified
            classify?.considers = consider
            classify?.image = imageViewToByte(binding.imageView)

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(EXTRA_REGISTRATION, classify)
            intent.putExtra(EXTRA_POSITION, position)
            val values = ContentValues()
            values.put(ClassifybaseRegsiter.ClassifyColumns.CLASSIFIED, classify?.classified)
            values.put(ClassifybaseRegsiter.ClassifyColumns.CONSIDER, classify?.considers)
            values.put(ClassifybaseRegsiter.ClassifyColumns.IMAGE, classify?.image)

            if(isEdit) {
                val result = classifyHelper.update(
                    classify?.id.toString(),
                    values
                ).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                }
            } else {
                val result = classifyHelper.insert(values)
                if (result > 0) {
                    classify?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    Log.d("sukses add", "$result")
                    Toast.makeText(
                        this@CoffeClasifyActivity,
                        "Sukses menambah data",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@CoffeClasifyActivity,
                        "Gagal menambah data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun showMessage(message: String) {
        Toast.makeText(this@CoffeClasifyActivity, message, Toast.LENGTH_LONG).show()
    }
    companion object {
        private const val TAG = "ANDROID ADMOB SAMPLE"
        private const val INTERSTITIAL_AD = "ca-app-pub-3940256099942544/1033173712"
        private const val REWARDED_AD = "ca-app-pub-3940256099942544/5224354917"
    }
}