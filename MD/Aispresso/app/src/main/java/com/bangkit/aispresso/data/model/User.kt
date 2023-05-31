package com.bangkit.aispresso.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    var email : String ?= "",
    var password : String ?= "",
    var url : String ?= "",
    var username : String ?= "",
    var telepon : String ?= "",
) : Parcelable