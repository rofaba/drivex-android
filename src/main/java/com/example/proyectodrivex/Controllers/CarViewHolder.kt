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
        binding.name.text = "${car.brand} ${car.model}"
        binding.price.text = "${car.price} €"
        binding.km.text = "${car.km} km"
        binding.hp.text = "${car.hp} hp"

        if (car.image.isNotEmpty()) {
            Picasso.get()
                .load(car.image)
                .fit()
                .centerCrop()
                .into(binding.image)
        }

        itemView.setOnClickListener {
            onClickListener(car)
        }
    }
}