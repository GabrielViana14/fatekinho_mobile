package com.fatec.fatekinho

import DatabaseHelper
import android.health.connect.datatypes.units.Length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbarIcon: ImageView
    // Defina as informações de conexão
    private val url = "jdbc:sqlserver://fatekinho-fatec.database.windows.net:1433;database=fatekinho;encrypt=false"
    private val user = "admin1"
    private val password = "Admin@2024"
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        // Inicialize o DatabaseHelper
        databaseHelper = DatabaseHelper(url, user, password)

        // Conectar ao banco de dados
        """databaseHelper.connect { message ->
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }"""


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbarIcon = findViewById(R.id.toolbar_logo)


        //faz o drawer pode ser aberto puxando
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // '?' diz que a variavel pode ser vazia
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        // lidar com botões dentro do nav drawer
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())


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



}