package com.fatec.fatekinho

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.fatec.fatekinho.databinding.ActivityUsersEditBinding
import com.fatec.fatekinho.models.UsuarioCreate
import com.fatec.fatekinho.models.Usuarios
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class UsersEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersEditBinding
    private var editOrCreate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUsersEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val idUsuario = intent.getIntExtra("idUsuario", -1)
        val email = intent.getStringExtra("email")
        val tipo = intent.getStringExtra("tipo")
        val senha = intent.getStringExtra("senha")

        if(idUsuario != -1){
            binding.usersEditTxtEmail.setText(email)
            binding.usersEditTxtSenha.setText(senha)
            binding.usersEditTxtTipo.setText(tipo)
            editOrCreate = true
        }
        binding.usersEditBtnConfirmar.setOnClickListener {
            val item1 = binding.usersEditTxtEmail.text.toString()
            val item2 = binding.usersEditTxtSenha.text.toString()
            val item3 = binding.usersEditTxtTipo.text.toString()
            if(editOrCreate == true){

                atualizarUsuario(idUsuario,item1,item2,item3)
            } else{
                criarUsuario(item1,item2,item3)
            }
        }

        binding.usersEditBtnVoltar.setOnClickListener{
            finish()
        }

    }

    private fun atualizarUsuario(idUsuario: Int, email: String?, senha: String?, tipo: String?) {
        if (email.isNullOrEmpty() || senha.isNullOrEmpty() || tipo.isNullOrEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show()
            return
        }

        val usuarioAtualizado = Usuarios(idUsuario,email,senha,tipo)
        RetroFitInstance.api.updateUsuarioByID(idUsuario,usuarioAtualizado).enqueue(object : retrofit2.Callback<Usuarios>{
            override fun onResponse(call: Call<Usuarios>, response: Response<Usuarios>) {
                if (response.isSuccessful){
                    Toast.makeText(this@UsersEditActivity, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    Log.e("Saida","${response.body()}")
                    finish()

                } else{
                    Toast.makeText(this@UsersEditActivity, "Erro ao atualizar usuário: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuarios>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@UsersEditActivity, "Falha na atualização do usuário", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun criarUsuario(email: String?, senha: String?, tipo: String?) {
        if (email.isNullOrEmpty() || senha.isNullOrEmpty() || tipo.isNullOrEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show()
            return
        }

        val usuarioCriar = UsuarioCreate(email,senha,tipo)
        RetroFitInstance.api.createUsuarioById(usuarioCriar).enqueue(object : retrofit2.Callback<Usuarios>{
            override fun onResponse(call: Call<Usuarios>, response: Response<Usuarios>) {
                if (response.isSuccessful){
                    Toast.makeText(this@UsersEditActivity, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show()
                    Log.e("Saida","${response.body()}")
                    finish()
                } else{
                    Toast.makeText(this@UsersEditActivity, "Erro ao criar usuário: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuarios>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@UsersEditActivity, "Falha na criação do usuário", Toast.LENGTH_SHORT).show()
            }

        })
    }


}