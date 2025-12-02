package com.example.proyectodrivex

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var listcars: MutableList<Car>
    private lateinit var adapter: CarAdapter

    private var miPopup: PopupWindow? = null
    private var isUserLoggedIn = false

    private var currentUser: User? = null
    private val BASE_URL = "https://drivex-backend-lpl0.onrender.com"

    // ESTA ES LA URL QUE DABA PROBLEMAS
    private val URL_DIFICIL = "https://darkorchid-chicken-425842.hostingersite.com/images/users/defaultuser.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)
        binding.rvPrincipal.layoutManager = layoutManager
        binding.rvPrincipal.setHasFixedSize(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

    // --- FUNCIÓN ESPECIAL PARA CARGAR LA IMAGEN BLOQUEADA ---
    // Esta función crea un cliente "hacker" que se disfraza de navegador Chrome
    private fun cargarImagenDificil(url: String, imageView: ImageView) {

        // 1. Creamos el cliente OkHttp con la cabecera User-Agent falsa
        val clienteHacker = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val requestOriginal = chain.request()
                val requestDisfrazada = requestOriginal.newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .build()
                chain.proceed(requestDisfrazada)
            }
            .build()

        // 2. Creamos una instancia de Picasso que use ese cliente
        val picassoHacker = Picasso.Builder(this)
            .downloader(OkHttp3Downloader(clienteHacker))
            .listener { _, uri, exception ->
                Log.e("IMG_HACKER", "Error cargando $uri: ${exception.message}")
            }
            .build()

        // 3. Cargamos la imagen forzando la red (sin caché)
        // Añadimos timestamp para evitar que la caché corrupta nos moleste
        val urlFresca = "$url?t=${System.currentTimeMillis()}"

        Log.d("IMG_HACKER", "Intentando descargar: $urlFresca")

        picassoHacker
            .load(urlFresca)
            .fit()
            .centerCrop()
            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    Log.d("IMG_HACKER", "¡ÉXITO! Imagen cargada correctamente.")
                }

                override fun onError(e: Exception?) {
                    Log.e("IMG_HACKER", "Falló incluso con el truco: ${e?.message}")
                }
            })
    }

    private fun setupHeaderInteractions() {
        updateHeadervisibility()

        binding.header.btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
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
        if (isUserLoggedIn && currentUser != null) {
            binding.header.btnLogin.visibility = View.GONE
            binding.header.layoutlogeduser.visibility = View.VISIBLE

            binding.header.txtUserName.text = currentUser?.name ?: "Usuario"
            binding.header.imgUserAvatar.setImageDrawable(null)

            var imageUrlToLoad: String? = null

            // Prioridad a la imagen del usuario
            if (!currentUser?.images.isNullOrEmpty()) {
                imageUrlToLoad = currentUser?.images!![0].imageUrl
            } else if (!currentUser?.image.isNullOrEmpty()) {
                imageUrlToLoad = currentUser?.image
            }

            if (!imageUrlToLoad.isNullOrEmpty()) {
                val cleanUrl = imageUrlToLoad!!.trim()
                val fullUrl = if (cleanUrl.startsWith("http")) cleanUrl else BASE_URL + cleanUrl

                // USAMOS LA FUNCIÓN ESPECIAL AQUÍ
                cargarImagenDificil(fullUrl, binding.header.imgUserAvatar)
            } else {
                // Si el usuario no tiene foto, cargamos la de "Defecto" que me diste
                // USAMOS LA FUNCIÓN ESPECIAL TAMBIÉN AQUÍ PORQUE ESA URL DABA PROBLEMAS
                cargarImagenDificil(URL_DIFICIL, binding.header.imgUserAvatar)
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

        viewMenu.findViewById<View>(R.id.menuAccount).setOnClickListener {
            miPopup?.dismiss()
            val intent = Intent(this, Userview::class.java)
            if (currentUser != null) {
                intent.putExtra("user_data", currentUser)
            }
            startActivity(intent)
        }

        viewMenu.findViewById<View>(R.id.menuFavourites).setOnClickListener {
            miPopup?.dismiss()
            val intent = Intent(this, Favourites::class.java)
            startActivity(intent)
        }

        viewMenu.findViewById<View>(R.id.menuSearch).setOnClickListener {
            miPopup?.dismiss()
            val intent = Intent(this, Search::class.java)
            startActivity(intent)
        }

        viewMenu.findViewById<View>(R.id.menuCloseSession).setOnClickListener {
            miPopup?.dismiss()
            isUserLoggedIn = false
            currentUser = null
            updateHeadervisibility()
            Toast.makeText(this, "Session closed", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        viewMenu.findViewById<View>(R.id.menuHome).setOnClickListener {
            miPopup?.dismiss()
            loadData()
            Toast.makeText(this, "Home refreshed", Toast.LENGTH_SHORT).show()
        }

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