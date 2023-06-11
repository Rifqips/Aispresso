package com.bangkit.aispresso.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.aispresso.data.model.counsultant.Data
import com.bangkit.aispresso.data.model.counsultant.ItemConsultant
import com.bangkit.aispresso.data.network.counsultant.RetrofitCounsultant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelCounsultant : ViewModel()  {

    var liveDataCounsultant: MutableLiveData<ItemConsultant> = MutableLiveData()
    var loading = MutableLiveData<Boolean>()

    init {
        loading = MutableLiveData()
        callApiCounsultant()
    }

    fun getCounsultant():MutableLiveData<ItemConsultant>{
        return liveDataCounsultant
    }

    fun callApiCounsultant(){
        RetrofitCounsultant.instance.getAllCounsultant()
            .enqueue(object : Callback<ItemConsultant> {
                override fun onResponse(
                    call: Call<ItemConsultant>,
                    response: Response<ItemConsultant>
                ) {
                    if (response.isSuccessful){
                        liveDataCounsultant.postValue(response.body())
                        Log.d("data-muncul",response.body()?.data.toString())
                    }else{
                        Log.d("data-tidakmuncul",response.body()?.data.toString())
                    }
                }

                override fun onFailure(call: Call<ItemConsultant>, t: Throwable) {
                    Log.d("data",call.toString())
                }

            })

    }


}