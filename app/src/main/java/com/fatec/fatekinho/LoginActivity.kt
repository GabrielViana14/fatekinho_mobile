package com.fatec.fatekinho

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
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
    private lateinit var txt_senha: EditText

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txt_senha = findViewById(R.id.login_txt_senha)

        // Função para mostrar ou ocultar senha
        txt_senha.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = 2
                if (event.rawX >= (txt_senha.right - txt_senha.compoundDrawables[drawableRight].bounds.width())) {
                    if (txt_senha.transformationMethod == null) {
                        txt_senha.transformationMethod = PasswordTransformationMethod.getInstance()
                        txt_senha.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(R.drawable.baseline_toggle_off_48), null)
                    } else {
                        txt_senha.transformationMethod = null
                        txt_senha.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(R.drawable.toggle_on_maior), null)
                    }
                }
            }
            false
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
