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

                // Buscar usuario por nombre o email
                val userFound = users.find { user ->
                    user.email.equals(inputUser, ignoreCase = true) ||
                            user.name.equals(inputUser, ignoreCase = true)
                }

                if (userFound != null) {

                    // --- CORRECCIÓN DE SEGURIDAD ---
                    // Verificamos que la contraseña del servidor NO esté vacía antes de llamar a BCrypt
                    val serverPassword = userFound.password

                    if (serverPassword.isNullOrEmpty()) {
                        Toast.makeText(this@Login, "Error: Este usuario tiene datos corruptos (sin contraseña)", Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    // --------------------------------

                    val passwordMatch = try {
                        org.mindrot.jbcrypt.BCrypt.checkpw(inputPass, serverPassword)
                    } catch (e: IllegalArgumentException) {
                        // Si no es un hash válido, probamos comparación simple por si acaso
                        inputPass == serverPassword
                    } catch (e: Exception) {
                        // Cualquier otro error de la librería
                        false
                    }

                    if (passwordMatch) {
                        Toast.makeText(this@Login, "Bienvenido, ${userFound.name}!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Login, MainActivity::class.java)
                        intent.putExtra("user_data", userFound)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@Login, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@Login, "Error técnico: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}