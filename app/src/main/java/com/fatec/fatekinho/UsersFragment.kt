package com.fatec.fatekinho

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatec.fatekinho.adapters.AdapterListUsers
import com.fatec.fatekinho.data_class.ListUser
import com.fatec.fatekinho.models.Cliente
import com.fatec.fatekinho.models.Usuarios
import retrofit2.Call
import retrofit2.Response


class UsersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<Usuarios>
    lateinit var mainTxt: Array<String>
    lateinit var SecondaryTxt: Array<String>
    private lateinit var countReg: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users,container,false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        countReg = view.findViewById(R.id.user_fragment_user_number)

        arrayList = arrayListOf()
        getUserData()




        return view
    }

    private fun getUserData() {
        RetroFitInstance.api.getAllUsuarios().enqueue(object : retrofit2.Callback<List<Usuarios>>{
            override fun onResponse(
                call: Call<List<Usuarios>>,
                response: Response<List<Usuarios>>
            ) {
                if(response.isSuccessful){
                    val users = response.body()
                    val total_reg = users?.size ?:0
                    if(users != null){
                        arrayList.addAll(users)
                        recyclerView.adapter = AdapterListUsers(arrayList){ user, action ->
                            when (action){
                                "editar" -> openEditScreen(user)
                            } 
                        }
                        countReg.text = total_reg.toString()


                    }
                }
            }

            override fun onFailure(call: Call<List<Usuarios>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private fun openEditScreen(user: Usuarios) {
        val intent = Intent(this.context, UsersDetailsActivity::class.java)
        intent.putExtra("idUsuario", user.idUsuario)
        intent.putExtra("email", user.email)
        startActivity(intent)

    }

}