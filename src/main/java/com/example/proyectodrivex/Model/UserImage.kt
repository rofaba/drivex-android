package com.example.proyectodrivex.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserImage(
    val id: Int? = 0,
    @SerializedName("imageUrl", alternate = ["url", "path"])
    val imageUrl: String? = ""
) : Serializable