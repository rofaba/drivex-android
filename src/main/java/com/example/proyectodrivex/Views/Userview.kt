package com.example.proyectodrivex.Views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectodrivex.Model.User
import com.example.proyectodrivex.R
import com.example.proyectodrivex.databinding.ActivityUserBinding
import com.squareup.picasso.Picasso

class Userview : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private var currentUser: User? = null

    // URL base para las imágenes
    private val BASE_URL = "https://drivex-backend-production.up.railway.app"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Recuperar datos del usuario que mandó el MainActivity
        recoverUserData()

        // 2. Mostrar datos en la pantalla
        setupUI()

        // 3. Activar botones
        setupListeners()
    }

    private fun recoverUserData() {
        if (intent.hasExtra("user_data")) {
            currentUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("user_data", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("user_data") as? User
            }
        }
    }

    private fun setupUI() {
        if (currentUser != null) {
            binding.AccNamebig.text = currentUser!!.name ?: "Usuario"
            binding.AccName.text = currentUser!!.name ?: "Usuario"
            binding.AccEmail.text = currentUser!!.email ?: "Sin email"

            // Lógica para cargar la imagen (igual que en MainActivity)
            var imageUrlToLoad: String? = null

            if (!currentUser!!.images.isNullOrEmpty()) {
                imageUrlToLoad = currentUser!!.images!![0].imageUrl
            } else if (!currentUser!!.image.isNullOrEmpty()) {
                imageUrlToLoad = currentUser!!.image
            }

            if (!imageUrlToLoad.isNullOrEmpty()) {
                val fullUrl = if (imageUrlToLoad!!.startsWith("http")) imageUrlToLoad else BASE_URL + imageUrlToLoad

                Picasso.get()
                    .load(fullUrl)
                    .placeholder(R.drawable.descarga) // Pon aquí tu imagen por defecto
                    .error(R.drawable.descarga)
                    .into(binding.AccProfImage)
            } else {
                binding.AccProfImage.setImageResource(R.drawable.descarga)
            }
        }
    }

    private fun setupListeners() {

        // --- AQUÍ ESTÁ EL BOTÓN QUE PEDISTE ---
        binding.AccChangePass.setOnClickListener {
            val intent = Intent(this, Editaccount::class.java)
            // Le pasamos los datos del usuario a la siguiente pantalla por si los necesita
            if (currentUser != null) {
                intent.putExtra("user_data", currentUser)
            }
            startActivity(intent)
        }
        // --------------------------------------

        // Botón Logout
        binding.Acclogout.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            // Limpiamos la pila para que no pueda volver atrás
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}