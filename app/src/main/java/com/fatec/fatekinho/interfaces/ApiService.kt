package com.fatec.fatekinho.interfaces

import com.fatec.fatekinho.data_class.LoginRequest
import com.fatec.fatekinho.data_class.LoginResponse
import com.fatec.fatekinho.data_class.TableUserDetails
import com.fatec.fatekinho.models.Cliente
import com.fatec.fatekinho.models.HistWonLose
import com.fatec.fatekinho.models.Usuarios
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("usuarios/{id}")
    fun getUsuariosById(@Path("id") userId: Int): Call<Usuarios>

    @POST("/login/")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("usuarios/all/")
    fun getAllUsuarios(): Call<List<Usuarios>>

    @GET("/histWonLose/all/")
    fun getAllHistWonLose(): Call<List<HistWonLose>>

    @GET("/table-comb/{id}")
    fun getTableCombById(@Path("id")userId: Int): Call<List<TableUserDetails>>

}