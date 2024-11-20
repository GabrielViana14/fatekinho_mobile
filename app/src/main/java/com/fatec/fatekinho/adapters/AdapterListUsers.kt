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

class AdapterListUsers(private val userlist: ArrayList<Usuarios>,
                       private val onButtonClick:(Usuarios, String) -> Unit
): RecyclerView.Adapter<AdapterListUsers.ViewHolderListUsers>() {

    class ViewHolderListUsers(itemView: View): RecyclerView.ViewHolder(itemView) {
        val MainTxt: TextView = itemView.findViewById(R.id.list_item_main_txt)
        val SecondaryTxt: TextView = itemView.findViewById(R.id.list_item_secon_txt)
        val btnEditar: ImageButton = itemView.findViewById(R.id.list_item_btn_editar)
        val btnApagar: ImageButton = itemView.findViewById(R.id.list_item_btn_apagar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderListUsers {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent,false)
        return ViewHolderListUsers(itemView)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(holder: ViewHolderListUsers, position: Int) {
        val currentItem = userlist[position]
        holder.MainTxt.text = currentItem.idUsuario.toString()
        holder.SecondaryTxt.text = currentItem.email

        holder.btnEditar.setOnClickListener {
            onButtonClick(currentItem, "editar")
        }
        holder.btnApagar.setOnClickListener {
            onButtonClick(currentItem, "apagar")
        }
    }
}