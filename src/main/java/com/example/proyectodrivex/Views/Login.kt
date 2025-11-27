package com.example.proyectodrivex.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectodrivex.Data.UserProvider
import com.example.proyectodrivex.MainActivity
import com.example.proyectodrivex.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        // 1. Botón de Login (Iniciar Sesión)
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        // 2. Botón de Sign Up (Ir al registro)
        binding.RegisterTextButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val inputUser = binding.LoginUser.text.toString().trim()
        val inputPass = binding.LoginPassword.text.toString().trim()

        if (inputUser.isEmpty() || inputPass.isEmpty()) {
            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val userProvider = UserProvider()
                val users = userProvider.getUsers()

                // Buscamos el usuario SOLO por nombre o email primero
                val userFound = users.find { user ->
                    user.email.equals(inputUser, ignoreCase = true) ||
                            user.name.equals(inputUser, ignoreCase = true)
                }

                if (userFound != null) {
                    // AQUÍ ESTÁ LA MAGIA:
                    // BCrypt coge tu contraseña plana (inputPass), la procesa y
                    // comprueba si coincide con el hash del JSON (userFound.password)
                    val passwordMatch = try {
                        BCrypt.checkpw(inputPass, userFound.password)
                    } catch (e: IllegalArgumentException) {
                        // Si la contraseña en el JSON no estaba hasheada (era texto plano "123")
                        // este bloque permite que funcione también (útil para pruebas)
                        inputPass == userFound.password
                    }

                    if (passwordMatch) {
                        // --- LOGIN CORRECTO ---
                        Toast.makeText(
                            this@Login,
                            "Bienvenido, ${userFound.name}!",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(this@Login, MainActivity::class.java)
                        intent.putExtra("user_data", userFound)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // Contraseña incorrecta
                        Toast.makeText(this@Login, "Contraseña incorrecta", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // Usuario no encontrado
                    Toast.makeText(this@Login, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@Login, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}