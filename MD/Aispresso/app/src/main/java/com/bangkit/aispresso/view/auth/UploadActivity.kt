package com.bangkit.aispresso.view.auth

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import com.bangkit.aispresso.data.model.user.User
import com.bangkit.aispresso.data.storage.PreferencesClass
import com.bangkit.aispresso.databinding.ActivityUploadBinding
import com.bangkit.aispresso.view.dashboard.DashboardActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*


class UploadActivity : AppCompatActivity(), PermissionListener {

    private lateinit var binding: ActivityUploadBinding

    lateinit var sUsername : String
    lateinit var sPassword : String
    lateinit var sEmail : String
    lateinit var sUrl : String
    lateinit var sTelepon : String

    private var imageMultiPart: MultipartBody.Part? = null
    private var imageFile: File? = null
    private var imageUri: Uri? = Uri.EMPTY

    var statusAdd: Boolean = false
    lateinit var filePath: Uri

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance : FirebaseDatabase
    lateinit var storageReference : StorageReference
    lateinit var preferences : PreferencesClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = PreferencesClass(this)
        storageReference = Firebase.storage.reference
        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("user")

        binding.tvDummyname.text = preferences.getValue("username")
        binding.tvDummytelpon.text = preferences.getValue("telepon")
        binding.tvDummyemail.text = preferences.getValue("email")
        binding.tvDummypassword.text = preferences.getValue("password")

        sUsername = binding.tvDummyname.text.toString()
        sPassword = binding.tvDummypassword.text.toString()
        sEmail = binding.tvDummyemail.text.toString()
        sTelepon = binding.tvDummytelpon.text.toString()

        sUrl = binding.imageView.toString()

        binding.ivAdd.setOnClickListener {
            openGallery()
        }

        binding.btnAction.setOnClickListener{

            // membuat proggres dialog
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            // dengan folder yang ada di firebasenya
            val ref = storageReference.child("images/"+ UUID.randomUUID().toString())
            ref.putFile(filePath) // kasih filenya dengan uri tadi / filepath
                .addOnSuccessListener {
                    // jika sukses matikan progress dialognya
                    progressDialog.dismiss()

                    // untuk url nya di save ke share preferences
                    ref.downloadUrl.addOnSuccessListener {
                        preferences.setValue("url", it.toString())
                        // preferences.setValue("url", it.toString())
//                        saveToFirebase(it.toString())
                    }
                }

                // jika tidak sukses
                .addOnFailureListener{ e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed" + e.message, Toast.LENGTH_LONG).show()
                }

                // untuk menampilkan berapa persen yang sudah terupload
                .addOnProgressListener {
                        taskSnapshot -> val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressDialog.setMessage("Upload "+progress.toInt()+" %")
                }

            saveUsername(sUsername, sPassword, sEmail, sUrl, sTelepon)

        }


    }

    fun openGallery(){
        getContent.launch("image/*")
    }
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver: ContentResolver = this!!.contentResolver
                val type = contentResolver.getType(it)
                imageUri = it

                val fileNameimg = "${System.currentTimeMillis()}.png"

                val imageView = binding.imageView
                imageView.setImageURI(it)

                Toast.makeText(this, "$imageUri", Toast.LENGTH_SHORT).show()

                val tempFile = File.createTempFile("aispresso-1", fileNameimg, null)
                imageFile = tempFile
                val inputstream = contentResolver.openInputStream(uri)
                tempFile.outputStream().use    { result ->
                    inputstream?.copyTo(result)
                }
                val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
                imageMultiPart = MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
            }
        }
    private fun saveUsername(sUsername: String, sPassword: String,  sEmail: String, sUrl: String, sTelepon : String) {
        val user = User()
        user.username = sUsername
        user.password = sPassword
        user.email = sEmail
        user.telepon = sTelepon
        user.url = sUrl

        checkingUsername(sUsername, user)
    }
    private fun checkingUsername(sUsername: String, data: User) {
        mFirebaseDatabase.child(sUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mFirebaseDatabase.child(sUsername).setValue(data)

                val intent = Intent(this@UploadActivity,
                    DashboardActivity::class.java).putExtra("data", data.username)
                startActivity(intent)
                Toast.makeText(this@UploadActivity, "Sukses Register", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@UploadActivity, ""+databaseError.message, Toast.LENGTH_LONG).show()
            }

        })
    }
    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        openGallery()
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan photo profile", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                // Image Uri will not be null for RESULT_OK
                statusAdd = true // status digunakan untuk menganti icon
                filePath = data?.data!!

                Glide.with(this)
                    .load(filePath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imageView)

                //  binding.ivAdd.setImageResource(R.drawableb)
                binding.btnAction.isGone = false

            }
            ImagePicker.IMAGE_TYPE_LOCK_SCREEN_BACKGROUND -> {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}