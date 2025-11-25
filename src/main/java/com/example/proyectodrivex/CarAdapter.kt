package com.example.proyectodrivex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(private var carsList: List<Car>,
                   private val onClickListener: (Car)-> Unit): RecyclerView.Adapter<CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CarViewHolder(layoutInflater.inflate(R.layout.activity_car, parent, false))
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val item = carsList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return carsList.size
    }

    fun setFilteredList(mList: MutableList<Car>){
        notifyItemRangeRemoved(0, mList.size)
        carsList = mList
        notifyItemRangeInserted(0, mList.size)
    }

}