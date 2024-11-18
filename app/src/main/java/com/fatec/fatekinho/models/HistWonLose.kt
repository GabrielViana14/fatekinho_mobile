package com.fatec.fatekinho.models


data class HistWonLose (
    val idJogo:Int,
    val idUsuario:Int,
    val valorApostado:Float,
    val ganhou:Int,
    val valorFinalApostado:Float,
    val dataCadastro:String
)