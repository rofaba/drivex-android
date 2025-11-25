package com.example.proyectodrivex.Model

import java.io.Serializable

data class User(val email: String, val user: String, val password: String, val profileimage: Int):
    Serializable