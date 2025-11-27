package com.example.proyectodrivex.Data

import com.example.proyectodrivex.Model.Car
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarProvider {
    suspend fun getCars(): List<Car> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getAllCars()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}