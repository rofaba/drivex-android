package com.example.proyectodrivex.Data

import com.example.proyectodrivex.Model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserProvider {

    // TIENE QUE SER SUSPEND PARA USAR CORRUTINAS
    suspend fun getUsers(): List<User> {
        // ESTA LÍNEA ES OBLIGATORIA: Mueve el trabajo a un hilo secundario (IO)
        return withContext(Dispatchers.IO) {
            try {
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