package com.example.proyectodrivex

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Controllers.CarAdapter
import com.example.proyectodrivex.Data.CarProvider
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.Model.User
import com.example.proyectodrivex.Views.Login
import com.example.proyectodrivex.Views.Userview
import com.example.proyectodrivex.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var listcars: MutableList<Car>
    private lateinit var listusers: MutableList<User>
    private lateinit var adapter: CarAdapter

    private var miPopup: PopupWindow? = null
    private var isUserLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvPrincipal.layoutManager = layoutManager

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupHeaderInteractions()
        loadData()
    }

    private fun setupHeaderInteractions() {
        updateHeadervisibility()

        binding.header.btnLogin.setOnClickListener {
            isUserLoggedIn = true
            updateHeadervisibility()
            Toast.makeText(this, "Log in successfully", Toast.LENGTH_SHORT).show()
        }

        binding.header.btnMenuDropdown.setOnClickListener { view ->
            if (miPopup == null || !miPopup!!.isShowing) {
                mostrarMenuDesplegable(view)
            } else {
                miPopup?.dismiss()
            }
        }
    }

    private fun updateHeadervisibility() {
        if (isUserLoggedIn) {
            binding.header.btnLogin.visibility = View.GONE
            binding.header.layoutlogeduser.visibility = View.VISIBLE
        } else {
            binding.header.btnLogin.visibility = View.VISIBLE
            binding.header.layoutlogeduser.visibility = View.GONE
        }
    }

    private fun mostrarMenuDesplegable(anchorView: View) {
        val inflater = LayoutInflater.from(this)
        val viewMenu = inflater.inflate(R.layout.popup_menu, null)

        miPopup = PopupWindow(
            viewMenu,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        miPopup?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        miPopup?.elevation = 20f

        viewMenu.findViewById<View>(R.id.menuAccount).setOnClickListener {
            miPopup?.dismiss()
            val intent = Intent(this, Userview::class.java)
            startActivity(intent)
        }

        viewMenu.findViewById<View>(R.id.menuFavourites).setOnClickListener {
            miPopup?.dismiss()
            Toast.makeText(this, "Favourites", Toast.LENGTH_SHORT).show()
        }

        viewMenu.findViewById<View>(R.id.menuSearch).setOnClickListener {
            miPopup?.dismiss()
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
        }

        viewMenu.findViewById<View>(R.id.menuMessages).setOnClickListener {
            miPopup?.dismiss()
            Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show()
        }

        viewMenu.findViewById<View>(R.id.menuMyAds).setOnClickListener {
            miPopup?.dismiss()
            Toast.makeText(this, "My Ads", Toast.LENGTH_SHORT).show()
        }

        viewMenu.findViewById<View>(R.id.menuSell).setOnClickListener {
            miPopup?.dismiss()
            Toast.makeText(this, "Sell", Toast.LENGTH_SHORT).show()
        }

        viewMenu.findViewById<View>(R.id.menuCloseSession).setOnClickListener {
            isUserLoggedIn = false
            updateHeadervisibility()
            miPopup?.dismiss()
            Toast.makeText(this, "Session closed", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        viewMenu.findViewById<View>(R.id.menuHome).setOnClickListener {
            miPopup?.dismiss()
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        }

        miPopup?.showAsDropDown(anchorView, -20, 10)
    }

    private fun loadData() {
        val carProvider = CarProvider()

        lifecycleScope.launch {
            val cars = carProvider.getCars()

            if (cars.isNotEmpty()) {

                adapter = CarAdapter(cars.toMutableList()) { car ->
                    val intent = Intent(this@MainActivity, com.example.proyectodrivex.Views.Details::class.java)
                    intent.putExtra("car", car)
                    startActivity(intent)
                }

                binding.rvPrincipal.adapter = adapter
                listcars = cars.toMutableList()
            } else {
                Toast.makeText(this@MainActivity, "Error cargando datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}