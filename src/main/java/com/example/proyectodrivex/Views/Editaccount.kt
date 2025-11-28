package com.example.proyectodrivex.Views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectodrivex.Model.User
import com.example.proyectodrivex.databinding.ActivityEditaccountBinding

class Editaccount : AppCompatActivity() {

    private lateinit var binding: ActivityEditaccountBinding
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditaccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Recuperamos los datos del usuario que nos pasó la pantalla anterior
        recoverUserData()

        // 2. Pintamos el email y nombre actuales en la pantalla
        setupUI()

        // 3. Activamos el botón
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
            binding.EdAcEmail.text = currentUser!!.email ?: "No email"
            binding.EdAcName.text = currentUser!!.name ?: "No name"
        }
    }

    private fun setupListeners() {
        binding.btnChangePassword.setOnClickListener {
            val currentPass = binding.Currentpass.text.toString()
            val newPass = binding.Newpass.text.toString()

            // Validación simple: que no estén vacíos
            if (currentPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // AQUÍ IRÍA LA LÓGICA DE API PARA CAMBIAR CONTRASEÑA (PUT)
            // Por ahora simulamos que ha ido bien:

            // 1. Mensaje de éxito
            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG).show()

            // 2. Redirigir a Userview (Activity User)
            val intent = Intent(this, Userview::class.java)

            // Le devolvemos los datos del usuario para que Userview no se quede vacío
            if (currentUser != null) {
                intent.putExtra("user_data", currentUser)
            }

            // Flag para limpiar la pantalla de edición de la pila (que no se pueda volver atrás)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)
            finish() // Cerramos esta actividad
        }
    }
}