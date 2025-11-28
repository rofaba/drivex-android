package com.example.proyectodrivex
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodrivex.Controllers.CarAdapter
import com.example.proyectodrivex.Data.CarProvider
import com.example.proyectodrivex.Model.Car
import com.example.proyectodrivex.Model.User
import com.example.proyectodrivex.Views.Details
import com.example.proyectodrivex.Views.Favourites
import com.example.proyectodrivex.Views.Login
import com.example.proyectodrivex.Views.Search
import com.example.proyectodrivex.Views.Userview
import com.example.proyectodrivex.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var listcars: MutableList<Car>
    private lateinit var adapter: CarAdapter

    private var miPopup: PopupWindow? = null
    private var isUserLoggedIn = false

    // Variables de usuario y URL base
    private var currentUser: User? = null
    private val BASE_URL = "https://drivex-backend-production.up.railway.app"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración RecyclerView
        layoutManager = LinearLayoutManager(this)
        binding.rvPrincipal.layoutManager = layoutManager
        binding.rvPrincipal.setHasFixedSize(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar datos del Login si existen
        if (intent.hasExtra("user_data")) {
            currentUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("user_data", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("user_data") as? User
            }

            if (currentUser != null) {
                isUserLoggedIn = true
            }
        }

        setupHeaderInteractions()
        loadData()
    }

    private fun setupHeaderInteractions() {
        updateHeadervisibility()

        // Botón Login (Rojo)
        binding.header.btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // Botón Flecha (Menú)
        binding.header.btnMenuDropdown.setOnClickListener { view ->
            if (miPopup == null || !miPopup!!.isShowing) {
                mostrarMenuDesplegable(view)
            } else {
                miPopup?.dismiss()
            }
        }
    }

    private fun updateHeadervisibility() {
        if (isUserLoggedIn && currentUser != null) {
            binding.header.btnLogin.visibility = View.GONE
            binding.header.layoutlogeduser.visibility = View.VISIBLE

            // Poner nombre
            binding.header.txtUserName.text = currentUser!!.name ?: "User"

            // Lógica para cargar la imagen (de lista o de string)
            var imageUrlToLoad: String? = null

            if (!currentUser!!.images.isNullOrEmpty()) {
                imageUrlToLoad = currentUser!!.images!![0].imageUrl
            } else if (!currentUser!!.image.isNullOrEmpty()) {
                imageUrlToLoad = currentUser!!.image
            }

            if (!imageUrlToLoad.isNullOrEmpty()) {
                // Si la URL es relativa, le pegamos la base
                val fullUrl = if (imageUrlToLoad!!.startsWith("http")) imageUrlToLoad else BASE_URL + imageUrlToLoad

                Picasso.get()
                    .load(fullUrl)
                    .placeholder(R.drawable.descarga) // Tu imagen por defecto
                    .error(R.drawable.descarga)
                    .into(binding.header.imgUserAvatar)
            } else {
                binding.header.imgUserAvatar.setImageResource(R.drawable.descarga)
            }

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

        // --- SOLO LAS OPCIONES QUE HAS DEJADO EN EL XML ---

        // 1. Account -> Ir a Userview
        viewMenu.findViewById<View>(R.id.menuAccount).setOnClickListener {
            miPopup?.dismiss()
            val intent = Intent(this, Userview::class.java)
            if (currentUser != null) {
                intent.putExtra("user_data", currentUser)
            }
            startActivity(intent)
        }

        // 2. Favourites -> (Solo mensaje por ahora)
        viewMenu.findViewById<View>(R.id.menuFavourites).setOnClickListener {
            miPopup?.dismiss()
            // CAMBIO AQUÍ: Navegar a FavouritesActivity
            val intent = Intent(this, Favourites::class.java)
            startActivity(intent)
        }

        // 3. Search -> (Solo mensaje por ahora)
        viewMenu.findViewById<View>(R.id.menuSearch).setOnClickListener {
            miPopup?.dismiss()

            val intent = Intent(this, Search::class.java)
            startActivity(intent)
        }

        // 4. Close Session -> Logout
        viewMenu.findViewById<View>(R.id.menuCloseSession).setOnClickListener {
            miPopup?.dismiss()

            isUserLoggedIn = false
            currentUser = null
            updateHeadervisibility()

            Toast.makeText(this, "Session closed", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // 5. Home -> Recargar datos
        viewMenu.findViewById<View>(R.id.menuHome).setOnClickListener {
            miPopup?.dismiss()
            loadData()
            Toast.makeText(this, "Home refreshed", Toast.LENGTH_SHORT).show()
        }

        // --- HE ELIMINADO MESSAGES, MY ADS Y SELL PORQUE YA NO ESTÁN EN TU XML ---

        miPopup?.showAsDropDown(anchorView, -20, 10)
    }

    private fun loadData() {
        val carProvider = CarProvider()

        lifecycleScope.launch {
            val cars = carProvider.getCars()

            if (cars.isNotEmpty()) {
                adapter = CarAdapter(cars.toMutableList()) { car ->
                    val intent = Intent(this@MainActivity, Details::class.java)
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