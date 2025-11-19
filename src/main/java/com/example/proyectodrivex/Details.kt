package com.example.proyectodrivex

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectodrivex.databinding.ActivityDetailsBinding
import com.squareup.picasso.Picasso


class Details : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val car = intent.getSerializableExtra("Car", Car::class.java)
        binding.Dtname.text= car!!.name
        Picasso.get()
            .load(car!!.image)
            .into(binding.Dtimage)
        binding.Dtdescription.text= "${car.description}"
        binding.Dtyear.text= "Year: ${car.year}"
        binding.Dtprice.text="Price: ${car.price}"
        binding.DtMileage.text="Mileage: ${car.km}"
    }
}