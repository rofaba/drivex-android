package com.example.proyectodrivex.Controllers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Model.User
import com.example.proyectodrivex.R

class UserAdapter(private var usersList: List<User>,
                  private val onClickListener: (User)-> Unit): RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.activity_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = usersList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    fun setFilteredList(mList: MutableList<User>){
        notifyItemRangeRemoved(0, mList.size)
        usersList = mList
        notifyItemRangeInserted(0, mList.size)
    }

}