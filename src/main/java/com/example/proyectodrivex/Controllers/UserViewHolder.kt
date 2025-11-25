package com.example.proyectodrivex.Controllers

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.Model.User

import com.example.proyectodrivex.databinding.ActivityUserBinding
import com.squareup.picasso.Picasso

class UserViewHolder (view: View): RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {

    private val binding = ActivityUserBinding.bind(view)


    fun render(item: User, onClickListener: (User) -> Unit) {
        binding.AccEmail.text = item.email
        Picasso.get()
            .load(item!!.profileimage)
            .into(binding.AccProfImage)
        binding.AccName.text = item.user
        binding.AccNamebig.text = item.user
        binding.Acclogout.setOnClickListener {}
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        TODO("Not yet implemented")
    }
}