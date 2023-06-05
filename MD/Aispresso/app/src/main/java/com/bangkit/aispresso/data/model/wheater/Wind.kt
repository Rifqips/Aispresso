package com.bangkit.aispresso.data.model.wheater

import java.io.Serializable

data class Wind(
    val speed: Double,
    val deg: Int
) : Serializable