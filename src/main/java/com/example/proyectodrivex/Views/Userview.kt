package com.example.proyectodrivex.Views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectodrivex.R
import com.example.proyectodrivex.databinding.ActivityUserBinding
import com.squareup.picasso.Picasso

class Userview : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListeners()
    }

    private fun initUI() {
        binding.AccNamebig.text = "Francisco Romero"
        binding.AccEmail.text = "francisco@gmail.com"
        binding.AccName.text = "Francisco"

        Picasso.get()
            .load("https://i.imgur.com/DvpvklR.png")
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.AccProfImage)
    }

    private fun initListeners() {
        binding.Acclogout.setOnClickListener {
            val intent = Intent(this, Login::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}