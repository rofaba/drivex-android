package com.example.proyectodrivex.Model
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    val id: Int? = 0,

    @SerializedName("username", alternate = ["name", "user", "full_name"])
    val name: String? = "",

    val email: String? = "",

    @SerializedName("password", alternate = ["pass", "contraseña", "password_hash"])
    val password: String? = "",

    // CAMBIO AQUÍ: Añadido "ProfileImage" como nombre principal
    @SerializedName("profileImage", alternate = ["image", "imageUrl", "profile_image", "avatar"])
    val image: String? = null,

    // Mantenemos la lista por si acaso el JSON devuelve array de imágenes
    @SerializedName("images")
    val images: List<UserImage>? = null

) : Serializable