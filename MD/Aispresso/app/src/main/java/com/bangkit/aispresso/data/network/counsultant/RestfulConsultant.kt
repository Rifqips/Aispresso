package com.bangkit.aispresso.data.network.counsultant

import com.bangkit.aispresso.data.model.counsultant.ItemConsultant
import retrofit2.Call
import retrofit2.http.GET

interface RestfulConsultant {
    @GET("/api/users?page=2")
    fun getAllCounsultant() : Call<ItemConsultant>
}