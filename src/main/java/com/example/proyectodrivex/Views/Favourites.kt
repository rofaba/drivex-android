package com.example.proyectodrivex.Views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodrivex.Controllers.CarAdapter
import com.example.proyectodrivex.Data.Favourites
import com.example.proyectodrivex.databinding.ActivityFavouritesBinding

class Favourites : AppCompatActivity() {

    private lateinit var binding: ActivityFavouritesBinding
    private lateinit var adapter: CarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView
        binding.rvFavourites.layoutManager = LinearLayoutManager(this)
        binding.rvFavourites.setHasFixedSize(true)

        cargarFavoritos()
    }

    override fun onResume() {
        super.onResume()
        // Recargamos cada vez que la pantalla vuelve a ser visible
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        // 1. Pedimos la lista al Singleton
        val listaFavoritos = Favourites.getAll()

        if (listaFavoritos.isNotEmpty()) {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvFavourites.visibility = View.VISIBLE

            // 2. Reutilizamos tu CarAdapter existente
            adapter = CarAdapter(listaFavoritos) { car ->
                // Al hacer clic, vamos al detalle igual que en el Main
                val intent = Intent(this, Details::class.java)
                intent.putExtra("car", car)
                startActivity(intent)
            }
            binding.rvFavourites.adapter = adapter
        } else {
            // Si no hay favoritos, mostramos mensaje
            binding.rvFavourites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        }
    }
}