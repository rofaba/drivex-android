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

        val car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("car", Car::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("car") as? Car
        }

        if (car != null) {
            binding.Dtname.text = "${car.brand} ${car.model}"
            binding.Dtdescription.text = car.description
            binding.Dtyear.text = "Year: ${car.year}"
            binding.Dtprice.text = "Price: ${car.price} €"
            binding.DtMileage.text = "Mileage: ${car.km} km"

            // Cargar imagen solo desde URL JSON
            if (!car.images.isNullOrEmpty()) {
                val urlFromJson = car.images[0].imageUrl

                val finalUrl = if (urlFromJson.startsWith("http")) {
                    urlFromJson
                } else {
                    BASE_URL + urlFromJson
                }

                Picasso.get()
                    .load(finalUrl)
                    .fit()
                    .centerCrop()
                    .into(binding.Dtimage)
            }
        }
    }
}