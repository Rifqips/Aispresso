package com.bangkit.aispresso.view.dashboard.wheaterfragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bangkit.aispresso.R
import com.bangkit.aispresso.data.model.wheater.WeatherResponse
import com.bangkit.aispresso.data.network.WeatherService
import com.bangkit.aispresso.data.utils.Constanta
import com.bangkit.aispresso.databinding.FragmentNotificationBinding
import com.bangkit.aispresso.databinding.FragmentWheaterBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class WheaterFragment : Fragment() {

    private lateinit var binding: FragmentWheaterBinding

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var mProgressDialog: Dialog? = null
    private lateinit var mSharedPreferences: SharedPreferences

    // declaring variables
    private val CHANNEL_ID = "chanel_id_example_01"
    private val notificationId = 101


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWheaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mSharedPreferences = requireActivity().getSharedPreferences(Constanta.PREFERENCE_NAME, Context.MODE_PRIVATE)

        setupUI()


        if (!isLocationEnabled()) {
            Toast.makeText(
                requireActivity(),
                "Your location provider is turned off. Please turn it on.",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            Dexter.withContext(requireActivity()).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report?.areAllPermissionsGranted() == true) {
                            requestLocationData()
                        }

                        if (report?.isAnyPermissionPermanentlyDenied == true) {
                            Toast.makeText(
                                requireActivity(),
                                "You have denied location permission. Please allow",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread().check()

        }

//        createNotificationChannel()
//        sendNotification()

    }

//    private fun createNotificationChannel(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val name = "Notification Title"
//            val descriptionText = "Notification Description"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
//                description= descriptionText
//            }
//            val notificationManager: NotificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

//    private fun sendNotification(){
//
//        var temp = binding.tvTemp.text.toString().toFloat()
//
//        if (temp <= 22){
//            binding.textView.text = "warning"
//        } else if (temp >= 30){
//            binding.textView.text = "warning"
//        } else if (temp >= 26 || temp <= 30 ){
//            binding.textView.text = "pertahankan"
//        }
//
//        val builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_logo)
//            .setContentTitle("Temperatur saat ini adalah $temp")
//            .setContentText(binding.textView.text)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        with(NotificationManagerCompat.from(requireActivity())){
//            if (ActivityCompat.checkSelfPermission(
//                    requireActivity(),
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
//            notify(notificationId, builder.build())
//        }
//    }

    private fun getLocationWeatherDetails(latitude: Double, longitude: Double) {
        if (Constanta.isNetworkAvailable(requireActivity())) {
//            Toast.makeText(
//                this@MainActivity,
//                "You have connected to the internet",
//                Toast.LENGTH_LONG
//            ).show()
            val retrofit = Retrofit.Builder().baseUrl(Constanta.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val service: WeatherService =
                retrofit.create<WeatherService>(WeatherService::class.java)
            val listCall: Call<WeatherResponse> = service.getWeather(
                latitude, longitude, Constanta.METRIC_UNIT, Constanta.APP_ID
            )
            showCustomProgressDialog()

            listCall.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {

                        hideProgressDialog()
                        val weatherList: WeatherResponse? = response.body()

                        val weatherResponseJsonString = Gson().toJson(weatherList)
                        val editor = mSharedPreferences.edit()
                        editor.putString(Constanta.WEATHER_RESPONSE_DATA, weatherResponseJsonString)
                        editor.apply()

                        setupUI()
                        Log.i("Response Result", "$weatherList")
                    } else {
                        val rc = response.code()
                        when (rc) {
                            400 -> Log.e("Error 400", "Bad connection")
                            404 -> Log.e("Error 404", "Not found")
                            else -> Log.e("Error", "Generic error")
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    hideProgressDialog()
                }
            })

        } else {
            Toast.makeText(requireActivity(), "No internet connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun isLocationEnabled(): Boolean {

        //Provides access to the system location services.
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(requireActivity())
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLocation: Location = locationResult.lastLocation

            val latitude = mLocation.latitude
            Log.i("Current latitude", "$latitude")

            val longitude = mLocation.longitude
            Log.i("Current longitude", "$longitude")
            getLocationWeatherDetails(latitude, longitude)
        }
    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.setContentView(R.layout.dialog_custom_progress)
        mProgressDialog?.show()
    }

    private fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog?.dismiss()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
            requestLocationData()
            true
        }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    private fun setupUI() {
        val weatherResponseJsonString = mSharedPreferences.getString(Constanta.WEATHER_RESPONSE_DATA,"")

        if (!weatherResponseJsonString.isNullOrEmpty()) {
            val weatherList = Gson().fromJson(weatherResponseJsonString,WeatherResponse::class.java)
            for (i in weatherList?.weather?.indices!!) {
                Log.i("Weather Name", weatherList.weather.toString())
                binding.tvMain.text = weatherList.weather[i].main
                binding.tvMainDescription.text = weatherList.weather[i].description
                binding.tvTemp.text =
                    weatherList.main.temp.toString()
                binding.tvHumidity.text = weatherList.main.humidity.toString() + " per cent"
                binding.tvMin.text = weatherList.main.temp_min.toString() + " min"
                binding.tvMax.text = weatherList.main.temp_max.toString() + " max"
                binding.tvSpeed.text = weatherList.wind.speed.toString()
                binding.tvName.text = weatherList.name
                binding.tvCountry.text = weatherList.sys.country

                binding.tvSunriseTime.text = unixTime(weatherList.sys.sunrise)
                binding.tvSunsetTime.text = unixTime(weatherList.sys.sunset)

                when (weatherList.weather[i].icon) {
                    "01d" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "02d" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "03d" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "04d" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "04n" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "10d" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "11d" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "13d" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "01n" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "02n" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "03n" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "10n" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "11n" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "13n" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                    "50d" -> binding.ivMain.setImageResource(R.drawable.icon_awanhujan)
                }
            }
        }

    }

    private fun unixTime(timex: Long): String? {
        val date = Date(timex * 1000L)
        val sdf = SimpleDateFormat("HH:mm", Locale.UK)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun getUnit(value: String): String? {
        var rValue = "°C"
        if ("US" == value || "LR" == value || "MM" == value) {
            rValue = "°F"
        }
        return rValue
    }
}