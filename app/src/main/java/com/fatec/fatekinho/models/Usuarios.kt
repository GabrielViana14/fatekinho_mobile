package com.fatec.fatekinho.models

data class Usuarios(
    val idUsuario:Int,
    val email:String,
    val senha:String,
    val tipo:Int,
    val idCliente:Int
)
