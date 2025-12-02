package com.example.proyectodrivex.Controllers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Data.Favourites
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.databinding.ActivityCarBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ActivityCarBinding.bind(view)

    // URL base de tu API
    private val BASE_URL = "https://drivex-backend-lpl0.onrender.com"

    // URL de la imagen por defecto (si falla la del coche)
    private val ERROR_IMAGE_URL = "https://darkorchid-chicken-425842.hostingersite.com/images/vehicles/HYUNDAI-Accent-15-105/HYUNDAI-Accent-15-105-6928a076b612d.jpg"

    fun render(car: Car, onClickListener: (Car) -> Unit) {
        // Asignar textos
        binding.name.text = "${car.brand ?: ""} ${car.model ?: ""}"
        binding.price.text = "${car.price ?: 0} €"
        binding.km.text = "${car.km ?: 0} km"
        binding.hp.text = "${car.hp ?: 0} hp"

        // 1. Limpiar imagen anterior
        binding.image.setImageDrawable(null)

        // 2. Buscar la URL de la foto del coche (Plan A)
        var primaryUrlToLoad: String? = null

        if (!car.images.isNullOrEmpty()) {
            val urlFromJson = car.images[0].imageUrl.trim()

            // Si empieza por http usamos la del JSON, si no, le pegamos la base
            primaryUrlToLoad = if (urlFromJson.startsWith("http")) {
                urlFromJson
            } else {
                BASE_URL + urlFromJson
            }
        }

        // 3. Intentar cargar
        if (!primaryUrlToLoad.isNullOrEmpty()) {
            // INTENTO CARGAR LA FOTO REAL
            Picasso.get()
                .load(primaryUrlToLoad)
                .fit()
                .centerCrop()
                .into(binding.image, object : Callback {
                    override fun onSuccess() {
                        // ¡Perfecto! Se cargó la foto del coche.
                    }

                    override fun onError(e: Exception?) {
                        // SI FALLA -> CARGAMOS LA IMAGEN POR DEFECTO
                        loadErrorImage()
                    }
                })
        } else {
            // SI EL COCHE NO TENÍA FOTO -> CARGAMOS LA IMAGEN POR DEFECTO
            loadErrorImage()
        }

        // --- LÓGICA DE FAVORITOS ---
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

    // Función auxiliar para cargar la imagen de la URL de defecto
    private fun loadErrorImage() {
        Picasso.get()
            .load(ERROR_IMAGE_URL)
            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE) // <--- ESTO OBLIGA A DESCARGAR
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .fit()
            .centerCrop()
            .into(binding.image, object : Callback {
                override fun onSuccess() {
                    // ¡Por fin cargó!
                }

                override fun onError(e: Exception?) {
                    // Si entra aquí ahora, es un problema real del servidor (ej: bloqueo de hotlink)
                    e?.printStackTrace()
                }
            })
    }

    private fun updateFavoriteIcon(car: Car) {
        if (Favourites.isFavorite(car)) {
            binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }
}