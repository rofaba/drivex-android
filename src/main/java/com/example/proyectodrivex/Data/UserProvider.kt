package com.example.proyectodrivex.Data

import com.example.proyectodrivex.Model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserProvider {

    suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                // Simplemente descargamos la lista.
                // Si la API ya trae las imágenes dentro, no hay que hacer nada más.
                val response = RetrofitClient.instance.getAllUsers()

                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}