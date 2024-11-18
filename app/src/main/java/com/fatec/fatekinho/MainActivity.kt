package com.fatec.fatekinho


import android.app.Activity
import android.content.Intent
import android.database.SQLException
import android.health.connect.datatypes.units.Length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.sql.DriverManager
import java.util.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbarIcon: ImageView
    private lateinit var btn_login: Button
    private lateinit var btnCadastro:Button
    private lateinit var txtUsername: TextView
    private var txtLogin: String = "Teste"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawerLayout)
        txtUsername = findViewById(R.id.txt_username)
        toolbarIcon = findViewById(R.id.toolbar_logo)
        btnCadastro = findViewById(R.id.btn_cadastrar)
        btn_login = findViewById(R.id.btn_entrar)

        val token: String? = getAuthToken()  // Obtém o token
        if (token.isNullOrEmpty()) {  // Verifica se o token é nulo ou está vazio
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Fecha a activity atual para evitar que o usuário volte para ela
        }


        btn_login.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        //faz o drawer pode ser aberto puxando
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        // '?' diz que a variavel pode ser vazia
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Esconde texto
        txtUsername.visibility = View.GONE

        txtLogin = intent.getStringExtra("login").toString()

        if(txtLogin != "null"){
            txtUsername.text = txtLogin
            txtUsername.visibility = View.VISIBLE
            btn_login.visibility = View.GONE
            btnCadastro.visibility = View.GONE
        }

        // lidar com botões dentro do nav drawer
        navView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.logoff -> logoff()
                R.id.users_menu ->replaceFragment(UsersFragment())


            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Abrir nav drawer clicando no logo
        toolbarIcon.setOnClickListener{
            if(drawerLayout.isDrawerOpen(navView)){
                drawerLayout.closeDrawer(navView)
            } else {
                drawerLayout.openDrawer(navView)
            }
        }

        if (savedInstanceState == null){
            replaceFragment(HomeFragment())
            navView.setCheckedItem(R.id.home)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    // mudar de tela continuando com o mesmo top e drawer
    // basicamente só troca a parte do meio
    private fun replaceFragment(fragment: Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun logoff(){
        txtLogin = ""
        txtUsername.visibility = View.GONE
        btnCadastro.visibility = View.VISIBLE
        btn_login.visibility = View.VISIBLE

    }

    // Recuperar o token de SharedPreferences
    fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }


}