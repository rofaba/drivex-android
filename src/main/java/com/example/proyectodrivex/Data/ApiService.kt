package com.example.proyectodrivex.Data

import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.Model.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/vehicles")
    suspend fun getAllCars(): Response<List<Car>>

    @GET("api/users")
    suspend fun getAllUsers(): Response<List<User>>

}