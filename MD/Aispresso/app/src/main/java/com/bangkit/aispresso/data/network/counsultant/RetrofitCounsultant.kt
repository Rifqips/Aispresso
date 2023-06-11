package com.bangkit.aispresso.data.network.counsultant

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCounsultant {


    const val BASE_URL = "https://reqres.in"

    val instance : RestfulConsultant by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RestfulConsultant::class.java)
    }

}