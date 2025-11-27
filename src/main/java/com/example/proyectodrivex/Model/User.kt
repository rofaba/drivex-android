package com.example.proyectodrivex.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    val id: Int? = 0,

    @SerializedName("username", alternate = ["name", "user", "full_name"])
    val name: String? = "",

    val email: String? = "",

    val password: String? = "",

    // --- CAMBIO IMPORTANTE ---
    // Ahora aceptamos una lista de imágenes, igual que en Car.
    // Si la API sigue mandando "image" como String antiguo, lo mantenemos por compatibilidad.
    @SerializedName("image")
    val image: String? = null,

    @SerializedName("images")
    val images: List<UserImage>? = null

) : Serializable