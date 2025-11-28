package com.example.proyectodrivex.Controllers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Data.Favourites
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

        binding.image.setImageDrawable(null)

        if (!car.images.isNullOrEmpty()) {
            val urlFromJson = car.images[0].imageUrl.trim()

            val finalUrl = if (urlFromJson.startsWith("http")) {
                urlFromJson
            } else {
                BASE_URL + urlFromJson
            }

            Picasso.get()
                .load(finalUrl)
                .fit()
                .centerCrop()
                .into(binding.image)
        }

        updateFavoriteIcon(car)

        binding.btnFavorite.setOnClickListener {
            if (Favourites.isFavorite(car)) {
                Favourites.remove(car)
            } else {
                Favourites.add(car)
            }
            updateFavoriteIcon(car)
        }

        itemView.setOnClickListener {
            onClickListener(car)
        }
    }

    private fun updateFavoriteIcon(car: Car) {
        if (Favourites.isFavorite(car)) {
            binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }
}