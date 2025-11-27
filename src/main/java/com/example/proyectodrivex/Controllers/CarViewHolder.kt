package com.example.proyectodrivex.Controllers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.databinding.ActivityCarBinding
import com.squareup.picasso.Picasso

class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ActivityCarBinding.bind(view)

    // Base URL por si acaso la ruta viene relativa
    private val BASE_URL = "https://drivex-backend-production.up.railway.app"

    fun render(car: Car, onClickListener: (Car) -> Unit) {
        binding.name.text = "${car.brand ?: ""} ${car.model ?: ""}"
        binding.price.text = "${car.price ?: 0} €"
        binding.km.text = "${car.km ?: 0} km"
        binding.hp.text = "${car.hp ?: 0} hp"

        // Limpiamos la imagen anterior por si acaso el reciclado de vistas falla
        binding.image.setImageDrawable(null)

        // Solo intentamos cargar si hay imágenes en la lista del JSON
        if (!car.images.isNullOrEmpty()) {
            val urlFromJson = car.images[0].imageUrl

            // Lógica: Si es absoluta (https://...) la usa, si es relativa (/images...) le pega la base
            val finalUrl = if (urlFromJson.startsWith("http")) {
                urlFromJson
            } else {
                BASE_URL + urlFromJson
            }

            // Picasso carga directamenta la URL sin placeholders ni errores locales
            Picasso.get()
                .load(finalUrl)
                .fit()
                .centerCrop()
                .into(binding.image)
        }

        itemView.setOnClickListener {
            onClickListener(car)
        }
    }
}