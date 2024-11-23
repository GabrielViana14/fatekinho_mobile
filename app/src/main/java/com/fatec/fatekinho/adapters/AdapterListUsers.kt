package com.fatec.fatekinho.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fatec.fatekinho.R
import com.fatec.fatekinho.models.Usuarios

class AdapterListUsers(
    private val userlist: ArrayList<Usuarios>,
    private val onButtonClick: (Usuarios, String) -> Unit,
    private val onFilterUpdate: (Int) -> Unit
) : RecyclerView.Adapter<AdapterListUsers.ViewHolderListUsers>() {

    private var filteredList = mutableListOf<Usuarios>()

    init {
        filteredList.addAll(userlist) // Inicializa com todos os itens
    }

    // Função para filtrar os itens da lista
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            ArrayList(userlist) // Se a consulta estiver vazia, exibe todos os itens
        } else {
            val lowerCaseQuery = query.lowercase()
            ArrayList(userlist.filter {
                it.email.lowercase().contains(lowerCaseQuery)
            })
        }
        onFilterUpdate(filteredList.size) // Atualizar a contagem de itens filtrados
        notifyDataSetChanged() // Notificar o adapter sobre a mudança
    }

    // ViewHolder para exibir os itens no RecyclerView
    class ViewHolderListUsers(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val MainTxt: TextView = itemView.findViewById(R.id.list_item_main_txt)
        val SecondaryTxt: TextView = itemView.findViewById(R.id.list_item_secon_txt)
        val btnEditar: ImageButton = itemView.findViewById(R.id.list_item_btn_editar)
        val btnApagar: ImageButton = itemView.findViewById(R.id.list_item_btn_apagar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderListUsers {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolderListUsers(itemView)
    }

    // Bind dos dados para exibir no RecyclerView
    override fun onBindViewHolder(holder: ViewHolderListUsers, position: Int) {
        val currentItem = filteredList[position]  // Usar a lista filtrada aqui
        holder.MainTxt.text = currentItem.idUsuario.toString()
        holder.SecondaryTxt.text = currentItem.email

        // Ações de clique nos botões de editar e apagar
        holder.btnEditar.setOnClickListener {
            onButtonClick(currentItem, "editar")
        }
        holder.btnApagar.setOnClickListener {
            onButtonClick(currentItem, "apagar")
        }
        holder.itemView.setOnClickListener {
            onButtonClick(currentItem, "detalhes")
        }
    }

    override fun getItemCount(): Int = filteredList.size // Usar a lista filtrada aqui
}
