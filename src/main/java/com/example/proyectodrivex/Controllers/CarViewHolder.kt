package com.example.proyectodrivex.Controllers

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.databinding.ActivityCarBinding
import com.squareup.picasso.Picasso

class CarViewHolder (view: View): RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {

        private val binding = ActivityCarBinding.bind(view)



    fun render(item: Car, onClickListener: (Car)-> Unit){
        binding.name.text=item.name
        Picasso.get()
            .load(item!!.image)
            .into(binding.image)
        binding.price.text= item.price.toString()
        binding.km.text= item.km.toString()
        binding.hp.text= item.hp.toString()
        itemView.setOnClickListener {
            onClickListener(item)
        }
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        TODO("Not yet implemented")
    }


}