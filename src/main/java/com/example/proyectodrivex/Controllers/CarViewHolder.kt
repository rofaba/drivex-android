package com.example.proyectodrivex.Controllers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.databinding.ActivityCarBinding
import com.squareup.picasso.Picasso

class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ActivityCarBinding.bind(view)
    private val BASE_URL = "https://drivex-backend-production.up.railway.app"

    fun render(car: Car, onClickListener: (Car) -> Unit) {
        binding.name.text = "${car.brand ?: ""} ${car.model ?: ""}"
        binding.price.text = "${car.price ?: 0} €"
        binding.km.text = "${car.km ?: 0} km"
        binding.hp.text = "${car.hp ?: 0} hp"

        // LÓGICA NUEVA:
        // Verificamos si la lista de imágenes existe y no está vacía
        if (!car.images.isNullOrEmpty()) {

            // Cogemos la primera imagen de la lista (index 0)
            val firstImage = car.images[0].imageUrl

            val fullUrl = BASE_URL + firstImage

            Picasso.get()
                .load(fullUrl)
                .fit()
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(binding.image)
        } else {
            // Imagen por defecto si la lista está vacía
            binding.image.setImageResource(android.R.drawable.ic_menu_camera)
        }

        itemView.setOnClickListener {
            onClickListener(car)
        }
    }
}