package com.fatec.fatekinho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class LoginActivity : AppCompatActivity() {

    private lateinit var btnEntrar: Button
    private lateinit var txtEmail: EditText
    private lateinit var txtSenha: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnEntrar = findViewById(R.id.login_btn_entrar)
        txtEmail = findViewById(R.id.login_email_text)
        txtSenha = findViewById(R.id.login_senha_text)

        btnEntrar.setOnClickListener {
            val email = txtEmail.text.toString()
            val senha = txtSenha.text.toString()
            Login(email, senha)
        }
    }

    private fun Login(email: String, senha: String) {
        // Verificar se os campos não estão vazios
        if (email.isBlank() || senha.isBlank()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }
        if(email == "admin" && senha == "admin123"){
            Toast.makeText(this@LoginActivity,"Login efeito com sucesso",Toast.LENGTH_SHORT).show()

            // Volta para tela iniciar com informação do usuario
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            intent.putExtra("login","Admin")
            startActivity(intent)
            //Termina a Activity atual
            finish()
        } else{
            Toast.makeText(this@LoginActivity,"Senha incorreta!",Toast.LENGTH_SHORT).show()
        }
    }

}
