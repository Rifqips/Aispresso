package com.bangkit.aispresso.view.camera.coffeprocessing

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bangkit.aispresso.databinding.ActivityCoffeBinding
import com.bangkit.aispresso.ml.BirdsModel
import com.bangkit.aispresso.ml.Mlkopi
import com.bangkit.aispresso.ml.ModelKopi
import com.bangkit.aispresso.view.dashboard.DashboardActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException

class CoffeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCoffeBinding
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var buttonBack: ImageView
    private lateinit var outputTextView: TextView
    private var GALLERY_REQUEST_CODE = 123

    lateinit var bitmap_img: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoffeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        imageView = binding.imageView
        button = binding.bntCaptureImage
        buttonBack = binding.ivBack




        buttonBack.setOnClickListener {
            startActivity(Intent(this@CoffeActivity, DashboardActivity::class.java))
        }
        outputTextView = binding.outputTextView
        val buttonLoad = binding.btnLoadImage

        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                takePicturePreview.launch(null)
            } else {
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
        buttonLoad.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onResult.launch(intent)
            } else {
                requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        //to redirect user to google search for the scientific name
        outputTextView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${outputTextView.text}"))
            startActivity(intent)
        }

        //to download image when longPress on ImageView
        imageView.setOnLongClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return@setOnLongClickListener true
        }
    }

    //request camera permission
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                takePicturePreview.launch(null)
            } else {
                Toast.makeText(this, "Permission Denied! Try Again.", Toast.LENGTH_SHORT).show()
            }
        }

    //launch camera and take picture
    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap_img)
                outputGenerator(bitmap_img)
            }
        }

    //get image
    private val onResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i("TAG", "This is the result: ${result.data} ${result.resultCode}")
            onResultRecived(GALLERY_REQUEST_CODE, result)
        }

    private fun onResultRecived(requestCode: Int, result: ActivityResult?) {
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (result?.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        Log.i("TAG", "onResultRecived: $uri")
                        val bitmap =
                            BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        imageView.setImageBitmap(bitmap_img)
                        outputGenerator(bitmap_img)
                    }
                } else {
                    Log.e("TAG", "onActivityResult: error in selecting image")
                }
            }
        }
    }

//    private fun outputGenerator(bitmap: Bitmap) {
//        //declaring tensorflow lite model veriable
//        val birdsmodel = BirdsModel.newInstance(this)
//
//        // Converting bitmap into tensorflow image
//        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//        val tfimage = TensorImage.fromBitmap(newBitmap)
//
//        // Process the image using trained model and sort it in descending order
//        val outputs = birdsmodel.process(tfimage)
//            .probabilityAsCategoryList.apply {
//                sortByDescending { it.score }
//            }
//
//        // Getting result having high probability
//        val highProbabilityOutput = outputs[0]
//
//        // Setting output text
//        outputTextView.text = highProbabilityOutput.label
//        Log.i("TAG", "outputGenerator: $highProbabilityOutput")
//
//
//        // Releases model resources if no longer used.
//        birdsmodel.close()
//    }

//    private fun outputGenerator(bitmap: Bitmap) {
        //declaring tensorflow lite model veriable
//        val coffemodel = ModelKopi.newInstance(this)
//
//        // Converting bitmap into tensorflow image
//        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//        val tfimage = TensorImage.fromBitmap(newBitmap)
//
//        val outputs = coffemodel.process(tfimage)
//
//        // Getting result having high probability on logcat
//        val highProbabilityOutput = outputs.probabilityAsTensorBuffer
//
//        // Setting output text
//        outputTextView.text = highProbabilityOutput.toString()
//        Log.i("TAG", "outputGenerator: $highProbabilityOutput")
//
//        // Releases model resources if no longer used.
//        coffemodel.close

//    }

    private fun outputGenerator(bitmap: Bitmap) {

        //resize uploadedimage
        var resized_bitmap_img: Bitmap = Bitmap.createScaledBitmap(bitmap_img, 224, 224,true)

        //paste the tflite model code here
        try{
            val model = Mlkopi.newInstance(this)

        // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

            //create bytebuffer image from the resized bitmap
            var byteBuffer= TensorImage.fromBitmap(resized_bitmap_img).buffer
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            Log.i("TAG", "outputGenerator: $outputFeature0")
            outputTextView.text = outputFeature0.toString()

            // Releases model resources if no longer used.
            model.close()
        }catch(e: Exception){
            Log.d(TAG, "exception in using model for predictions: "+e)
        }

    }

    //to download to device
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted: Boolean ->
        if (isGranted){
            AlertDialog.Builder(this).setTitle("Download Image?")
                .setMessage("Do you want to download this image to your device?")
                .setPositiveButton("Yes"){_, _ ->
                    val drawable: BitmapDrawable = imageView.drawable as BitmapDrawable
                    val bitmap = drawable.bitmap
                    downloadImage(bitmap)
                }
                .setNegativeButton("No"){dialog, _ ->
                    dialog.dismiss()
                }.show()
        }else{
            Toast.makeText(this, "Please allow permission to download image", Toast.LENGTH_LONG).show()
        }
    }

    //fun that takes a bitmap and store to user's device
    private fun downloadImage(mBitmap: Bitmap): Uri?{
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "coffe_Images"+System.currentTimeMillis()/1000)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        if (uri!=null){
            contentResolver.insert(uri, contentValues)?.also {
                contentResolver.openOutputStream(it).use { outputStream->
                    if (!bitmap_img.compress(Bitmap.CompressFormat.PNG, 100, outputStream)){
                        throw IOException("Could'nt save the bitmap")
                    }else{
                        Toast.makeText(applicationContext, "Image Saved", Toast.LENGTH_LONG).show()
                    }
                }
                return it
            }
        }
        return null
    }
}