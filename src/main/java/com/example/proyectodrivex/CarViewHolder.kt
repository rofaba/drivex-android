package com.example.proyectodrivex
import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
        p0: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        p0!!.setHeaderTitle(binding.name.text)
        p0.add(this.adapterPosition, 0, 0, "Account")
        p0.add(this.adapterPosition, 1 ,0, "Favourites")
        p0.add(this.adapterPosition, 2 ,0, "Search")
        p0.add(this.adapterPosition, 3 ,0, "Messages")
        p0.add(this.adapterPosition, 4 ,0, "My ads")
        p0.add(this.adapterPosition, 5 ,0, "Sell")
        p0.add(this.adapterPosition, 6 ,0, "Log off")
        p0.add(this.adapterPosition, 7 ,0, "Home")
    }
}