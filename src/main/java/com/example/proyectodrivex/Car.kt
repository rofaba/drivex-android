package com.example.proyectodrivex

import java.io.Serializable

data class Car( val id: Int,val image: Int, val name: String,val price: Double, val hp: Int, val cm3: Int, val brand: String, val model: String, val colour: String,
                val description: String, val year: Int, val fueltype: String, val km: Int): Serializable