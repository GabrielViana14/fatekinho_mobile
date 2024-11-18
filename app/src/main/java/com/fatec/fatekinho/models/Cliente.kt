package com.fatec.fatekinho.models

data class Cliente (
    val idCliente: Int,
    val nome: String,
    val data_nasc: String,
    val cpf: Long,
    val cep: String,
    val numero: Int,
    val complemento: String

)