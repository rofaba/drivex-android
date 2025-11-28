package com.example.proyectodrivex.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodrivex.Controllers.CarAdapter
import com.example.proyectodrivex.Data.CarProvider
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.databinding.ActivitySearchBinding
import kotlinx.coroutines.launch
import java.util.Locale

class Search : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: CarAdapter

    // LISTA 1: Copia de seguridad con TODOS los coches (nunca se borra)
    private var fullList = mutableListOf<Car>()

    // LISTA 2: La que se muestra en el RecyclerView (se filtra)
    private var displayList = mutableListOf<Car>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        loadData()
        initSearchListener()
    }

    private fun initUI() {
        // Configuramos el RecyclerView
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.setHasFixedSize(true)

        // Inicializamos el adapter con la lista vacía por ahora
        adapter = CarAdapter(displayList) { car ->
            // Al hacer click, vamos a detalles igual que en el Main
            val intent = Intent(this, Details::class.java)
            intent.putExtra("car", car)
            startActivity(intent)
        }
        binding.rvSearch.adapter = adapter
    }

    private fun loadData() {
        val carProvider = CarProvider()

        lifecycleScope.launch {
            // Descargamos los coches de internet
            val cars = carProvider.getCars()

            if (cars.isNotEmpty()) {
                // 1. Guardamos todo en la copia de seguridad
                fullList.clear()
                fullList.addAll(cars)

                // 2. Inicialmente mostramos todos
                displayList.clear()
                displayList.addAll(cars)

                // 3. Avisamos al adaptador
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this@Search, "No se encontraron coches", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSearchListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Se ejecuta cuando le das a "Enter" en el teclado (opcional)
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            // Se ejecuta cada vez que escribes o borras una letra
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val searchText = query.lowercase(Locale.ROOT)

            // Limpiamos la lista visible
            displayList.clear()

            if (searchText.isEmpty()) {
                // Si el buscador está vacío, volvemos a mostrar TODOS (desde la copia de seguridad)
                displayList.addAll(fullList)
            } else {
                // Si hay texto, buscamos coincidencias en Marca o Modelo
                fullList.forEach { car ->
                    val brand = car.brand?.lowercase(Locale.ROOT) ?: ""
                    val model = car.model?.lowercase(Locale.ROOT) ?: ""

                    // Si la marca O el modelo contienen lo que escribiste
                    if (brand.contains(searchText) || model.contains(searchText)) {
                        displayList.add(car)
                    }
                }
            }
            // Refrescamos la lista visual
            adapter.notifyDataSetChanged()
        }
    }
}