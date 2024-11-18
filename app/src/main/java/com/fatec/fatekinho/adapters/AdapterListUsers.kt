package com.fatec.fatekinho.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fatec.fatekinho.R
import com.fatec.fatekinho.models.Cliente

class AdapterListUsers(private val userlist: ArrayList<Cliente>): RecyclerView.Adapter<AdapterListUsers.ViewHolderListUsers>() {

    class ViewHolderListUsers(itemView: View): RecyclerView.ViewHolder(itemView) {
        val MainTxt: TextView = itemView.findViewById(R.id.list_item_main_txt)
        val SecondaryTxt: TextView = itemView.findViewById(R.id.list_item_secon_txt)

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
        holder.MainTxt.text = currentItem.nome
        holder.SecondaryTxt.text = currentItem.cpf.toString()
    }
}