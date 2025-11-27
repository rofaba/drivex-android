package com.example.proyectodrivex.Data

import com.example.proyectodrivex.Data.RetrofitClient
import com.example.proyectodrivex.Model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserProvider {

    suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getAllUsers()
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

    suspend fun login(email: String, password: String): User? {
        val users = getUsers()
        return users.find { it.email == email && it.password == password }
    }
}