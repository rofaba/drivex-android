package com.example.proyectodrivex.Data
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://drivex-backend-lpl0.onrender.com/"

    // Configuración de "Paciencia" para que espere al servidor de Render
    private val client = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS) // Tiempo para conectar
        .readTimeout(100, TimeUnit.SECONDS)    // Tiempo para esperar la respuesta
        .writeTimeout(100, TimeUnit.SECONDS)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // <--- AÑADIMOS ESTO
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}