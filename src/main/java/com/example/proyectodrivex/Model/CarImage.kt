package com.example.proyectodrivex.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CarImage(
    val id: Int,
    // El JSON dice "imageUrl", así que aquí también
    @SerializedName("imageUrl")
    val imageUrl: String,
    val main: Boolean
) : Serializable