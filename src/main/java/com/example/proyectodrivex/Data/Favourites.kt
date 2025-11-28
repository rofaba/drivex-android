package com.example.proyectodrivex.Data

import com.example.proyectodrivex.Model.Car

object Favourites {
    private val favoriteCars = mutableListOf<Car>()

    fun add(car: Car) {
        if (!favoriteCars.contains(car)) {
            favoriteCars.add(car)
        }
    }

    fun remove(car: Car) {
        favoriteCars.remove(car)
    }

    fun isFavorite(car: Car): Boolean {
        return favoriteCars.any { it.id == car.id }
    }

    fun getAll(): List<Car> {
        return favoriteCars
    }
}