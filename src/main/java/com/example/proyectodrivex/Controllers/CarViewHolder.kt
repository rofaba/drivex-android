package com.example.proyectodrivex.Controllers

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.databinding.ActivityCarBinding
import com.squareup.picasso.Picasso


class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ActivityCarBinding.bind(view)

    fun render(car: Car, onClickListener: (Car) -> Unit) {
        binding.name.text = "${car.brand ?: ""} ${car.model ?: ""}"
        binding.price.text = "${car.price ?: 0} €"
        binding.km.text = "${car.mileage ?: 0} km"
        binding.hp.text = "${car.hp ?: 0} hp"

        android.util.Log.d("IMAGEN_DEBUG", "Cargando imagen: ${car.image}")

        if (!car.image.isNullOrEmpty()) {
            Picasso.get()
                .load(car.image)
                .fit()
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(binding.image)
        } else {
            binding.image.setImageResource(android.R.drawable.ic_menu_camera)
        }

        itemView.setOnClickListener {
            onClickListener(car)
        }
    }
}