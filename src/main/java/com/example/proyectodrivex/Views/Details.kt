package com.example.proyectodrivex.Views

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.databinding.ActivityDetailsBinding
import com.squareup.picasso.Picasso

class Details : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    // URL Base necesaria porque las imágenes vienen relativas (/images/...)
    private val BASE_URL = "https://drivex-backend-production.up.railway.app"

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

        // RECUPERAR EL COCHE DEL INTENT
        // Usamos una comprobación de versión para que funcione en todos los móviles
        val car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("car", Car::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("car") as? Car
        }

        // Si el coche ha llegado bien, rellenamos los datos
        if (car != null) {

            // 1. Textos
            binding.Dtname.text = "${car.brand ?: ""} ${car.model ?: ""}"
            binding.Dtdescription.text = car.description ?: "No description available"
            binding.Dtyear.text = "Year: ${car.year ?: 0}"
            binding.Dtprice.text = "Price: ${car.price ?: 0} €"
            binding.DtMileage.text = "Mileage: ${car.km ?: 0} km"

            // 2. Imagen (Lógica nueva para la lista de imágenes)
            if (!car.images.isNullOrEmpty()) {
                // Cogemos la primera imagen de la lista
                val firstImage = car.images[0].imageUrl
                val fullUrl = BASE_URL + firstImage

                Picasso.get()
                    .load(fullUrl)
                    .fit() // Ajusta la imagen
                    .centerCrop() // Recorta para llenar
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(binding.Dtimage)
            } else {
                // Imagen por defecto si no hay fotos
                binding.Dtimage.setImageResource(android.R.drawable.ic_menu_camera)
            }
        }
    }
}