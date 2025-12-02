package com.example.proyectodrivex.Views

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.R
import com.example.proyectodrivex.databinding.ActivityDetailsBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class Details : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    // TUS URLS
    private val BASE_URL = "https://drivex-backend-lpl0.onrender.com"
    private val ERROR_IMAGE_URL = "https://darkorchid-chicken-425842.hostingersite.com/images/vehicles/HYUNDAI-Accent-15-105/HYUNDAI-Accent-15-105-6928a076b612d.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar el coche
        val car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("car", Car::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("car") as? Car
        }

        if (car != null) {
            // Textos
            binding.Dtname.text = "${car.brand ?: ""} ${car.model ?: ""}"
            binding.Dtdescription.text = car.description ?: "No description"
            binding.Dtyear.text = "Year: ${car.year ?: 0}"
            binding.Dtprice.text = "Price: ${car.price ?: 0} €"
            binding.DtMileage.text = "Mileage: ${car.km ?: 0} km"

            // --- LÓGICA DE IMAGEN ---

            // 1. Limpiamos
            binding.Dtimage.setImageDrawable(null)

            // 2. Buscamos URL
            var primaryUrlToLoad: String? = null

            // Comprobamos que la lista no sea nula Y que la primera foto tenga texto
            if (!car.images.isNullOrEmpty() && !car.images[0].imageUrl.isNullOrBlank()) {
                val urlFromJson = car.images[0].imageUrl.trim()
                primaryUrlToLoad = if (urlFromJson.startsWith("http")) urlFromJson else BASE_URL + urlFromJson
                Log.d("DETAILS_DEBUG", "Plan A: Intentando cargar -> $primaryUrlToLoad")
            } else {
                Log.d("DETAILS_DEBUG", "El coche no tiene foto en el JSON.")
            }

            // 3. Ejecutamos carga
            if (!primaryUrlToLoad.isNullOrEmpty()) {
                Picasso.get()
                    .load(primaryUrlToLoad)
                    .fit()
                    .centerCrop()
                    .into(binding.Dtimage, object : Callback {
                        override fun onSuccess() {
                            Log.d("DETAILS_DEBUG", "Plan A: ¡Éxito!")
                        }

                        override fun onError(e: Exception?) {
                            Log.e("DETAILS_DEBUG", "Plan A: Falló. Causa: ${e?.message}. Pasando al Plan B.")
                            loadErrorImage()
                        }
                    })
            } else {
                // Si no había URL principal, cargamos la de defecto directamente
                Log.d("DETAILS_DEBUG", "Saltando directos al Plan B (Defecto)")
                loadErrorImage()
            }
        }
    }

    private fun loadErrorImage() {
        Picasso.get()
            .load(ERROR_IMAGE_URL)
            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE) // <--- FUERZA DESCARGA
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .fit()
            .centerCrop()
            .into(binding.Dtimage, object : Callback {
                override fun onSuccess() { }

                override fun onError(e: Exception?) {
                    e?.printStackTrace()
                }
            })
    }
}