package com.fatec.fatekinho

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatec.fatekinho.adapters.AdapterListUsers
import com.fatec.fatekinho.data_class.ListUser
import com.fatec.fatekinho.models.Cliente
import com.fatec.fatekinho.models.Usuarios
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Response


class UsersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<Usuarios>
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: AdapterListUsers
    private lateinit var btnCriar: MaterialButton
    private lateinit var countReg: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users,container,false)
        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.recyclerView)
        btnCriar = view.findViewById(R.id.rounded_icon_button)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        countReg = view.findViewById(R.id.user_fragment_user_number)

        arrayList = arrayListOf()
        adapter = AdapterListUsers(arrayList, { user, action ->
            when (action) {
                "detalhes" -> openDetailsScreen(user)
                "apagar" -> showPopupApagar(user)
                "editar" -> openEditScreen(user)
            }
        }) { filteredCount ->
            countReg.text = filteredCount.toString()
        }
        recyclerView.adapter = adapter
        getUserData()


        // Alterar a cor do texto no campo de pesquisa
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        searchEditText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))




        btnCriar.setOnClickListener {
            val intent = Intent(this.context, UsersEditActivity::class.java)
            startActivity(intent)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Filtrar quando o usuário pressiona "Enter"
                query?.let { adapter.filter(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    adapter.filter(newText ?: "")
                }
                handler.postDelayed(searchRunnable!!, 500) // Delay de 500ms
                return true
            }

        })


        return view
    }

    override fun onResume() {
        super.onResume()
        getUserData()
        searchView.setQuery(null, false)
        searchView.clearFocus()
    }

    private fun getUserData() {
        progressBar.visibility = View.VISIBLE
        RetroFitInstance.api.getAllUsuarios().enqueue(object : retrofit2.Callback<List<Usuarios>> {
            override fun onResponse(
                call: Call<List<Usuarios>>,
                response: Response<List<Usuarios>>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        arrayList.clear()
                        arrayList.addAll(it)
                        adapter.notifyDataSetChanged()
                        countReg.text = it.size.toString()
                        adapter.filter("")
                    }
                }
            }

            override fun onFailure(call: Call<List<Usuarios>>, t: Throwable) {
                progressBar.visibility = View.GONE
                t.printStackTrace()
                Toast.makeText(requireContext(), "Falha ao carregar dados.", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showPopupApagar(user: Usuarios) {
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_apagar,null)
        val idUsuario = user.idUsuario
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            true
        )

        val btnCancelar = popupView.findViewById<Button>(R.id.popup_btn_cancelar)
        val btnApagar = popupView.findViewById<Button>(R.id.popup_btn_apagar)

        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.showAtLocation(requireView(), Gravity.CENTER, 0, 0)

        btnCancelar.setOnClickListener {
            popupWindow.dismiss()
        }
        btnApagar.setOnClickListener {
            apagarUsuario(idUsuario)
            popupWindow.dismiss()
        }
    }

    private fun apagarUsuario(idUsuario: Int) {
        Toast.makeText(requireContext(), "Processando exclusão...", Toast.LENGTH_SHORT).show()
        RetroFitInstance.api.deleteUsuarioById(idUsuario).enqueue(object : retrofit2.Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 204 || response.isSuccessful) {
                    Toast.makeText(requireContext(), "Usuário excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    val position = arrayList.indexOfFirst { it.idUsuario == idUsuario }
                    if (position != -1) {
                        arrayList.removeAt(position)
                        adapter.notifyItemRemoved(position)
                    }
                    getUserData()
                } else {
                    Toast.makeText(requireContext(), "Erro ao excluir usuário: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Falha na exclusão do usuário.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun openDetailsScreen(user: Usuarios) {
        val intent = Intent(this.context, UsersDetailsActivity::class.java)
        intent.putExtra("idUsuario", user.idUsuario)
        intent.putExtra("email", user.email)
        intent.putExtra("tipo",user.tipo)
        startActivity(intent)

    }

    private fun openEditScreen(user: Usuarios) {
        val intent = Intent(this.context, UsersEditActivity::class.java)
        intent.putExtra("idUsuario", user.idUsuario)
        intent.putExtra("email", user.email)
        intent.putExtra("tipo",user.tipo)
        intent.putExtra("senha",user.senha)
        startActivity(intent)

    }

}