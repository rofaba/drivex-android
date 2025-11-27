package com.example.proyectodrivex.Controllers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.R

class CarAdapter(
    private val carList: List<Car>,
    private val onClickListener: (Car) -> Unit
) : RecyclerView.Adapter<CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CarViewHolder(layoutInflater.inflate(R.layout.activity_car, parent, false))
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val item = carList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = carList.size
}