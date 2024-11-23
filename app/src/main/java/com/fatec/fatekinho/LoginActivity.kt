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
import com.fatec.fatekinho.data_class.LoginRequest
import com.fatec.fatekinho.data_class.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var txtEmail: EditText
    private lateinit var txtSenha: EditText
    private lateinit var btnEntrar: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txtEmail = findViewById(R.id.login_txt_email)
        txtSenha = findViewById(R.id.login_txt_senha)
        btnEntrar = findViewById(R.id.login_btn_entrar)

        // Função para mostrar ou ocultar senha
        txtSenha.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = 2
                if (event.rawX >= (txtSenha.right - txtSenha.compoundDrawables[drawableRight].bounds.width())) {
                    if (txtSenha.transformationMethod == null) {
                        txtSenha.transformationMethod = PasswordTransformationMethod.getInstance()
                        txtSenha.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(R.drawable.baseline_toggle_off_48), null)
                    } else {
                        txtSenha.transformationMethod = null
                        txtSenha.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(R.drawable.toggle_on_maior), null)
                    }
                }
            }
            false
        }

        btnEntrar.setOnClickListener {
            val email = txtEmail.text.toString()
            val senha = txtSenha.text.toString()
            login(email, senha)
        }



    }

    private fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        val call = RetroFitInstance.api.login(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.token
                    saveAuthToken(token.toString())
                    // Armazene o token, ex: em SharedPreferences
                    // Toast.makeText(this@LoginActivity,"Token de autenticação: $token",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity,"Falha no login: ${response.code()}",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"Erro na chamada da API: ${t.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Salvar o token em SharedPreferences
    fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }


}
