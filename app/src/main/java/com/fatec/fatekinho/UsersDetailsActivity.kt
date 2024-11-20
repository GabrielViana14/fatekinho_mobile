package com.fatec.fatekinho

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatec.fatekinho.RetroFitInstance
import com.fatec.fatekinho.adapters.TableUserDetailsAdapter
import com.fatec.fatekinho.data_class.TableUserDetails
import com.fatec.fatekinho.databinding.ActivityUsersDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersDetailsBinding
    private lateinit var arrayList: ArrayList<TableUserDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar o View Binding
        binding = ActivityUsersDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idUsuario = intent.getIntExtra("idUsuario", -1)
        val email = intent.getStringExtra("email")
        Log.d("UsersDetailsActivity", "idUsuario: $idUsuario, email: $email")


        val data = listOf(
            TableUserDetails(1, "Entrada", "Engenheira"),
            TableUserDetails(1, "Entrada", "Engenheira"),
            TableUserDetails(1, "Entrada", "Engenheira")
        )

        binding.recyclerViewUserDetails.layoutManager = LinearLayoutManager(this)

        arrayList = arrayListOf()
        getUserdataCombined(idUsuario)
    }

    private fun getUserdataCombined(id: Int) {
        RetroFitInstance.api.getTableCombById(id).enqueue(object : Callback<List<TableUserDetails>> {  // Especificando o tipo correto
            override fun onResponse(
                call: Call<List<TableUserDetails>>,
                response: Response<List<TableUserDetails>>
            ) {
                if (response.isSuccessful) {
                    val itens = response.body()
                    if (itens != null) {
                        arrayList.addAll(itens)
                        binding.recyclerViewUserDetails.adapter = TableUserDetailsAdapter(arrayList)
                        binding.recyclerViewUserDetails.adapter?.notifyDataSetChanged()

                    }
                }
            }

            override fun onFailure(call: Call<List<TableUserDetails>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@UsersDetailsActivity, "Erro ao buscar dados: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
