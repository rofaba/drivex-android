package com.example.proyectodrivex.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Car(
    val id: Int? = 0,
    val brand: String? = "",
    val model: String? = "",
    val hp: Int? = 0,
    val price: Double? = 0.0,
    val year: Int? = 0,

    // Mapeamos "mileage" del JSON a tu variable "km" antigua
    @SerializedName("mileage")
    val km: Int? = 0,

    @SerializedName("fuel_type")
    val fueltype: String? = "",

    val description: String? = "",

    // AQUÍ ESTÁ EL CAMBIO: Recibimos una LISTA de objetos imagen
    val images: List<CarImage>? = null

) : Serializable