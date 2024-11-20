package com.fatec.fatekinho.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fatec.fatekinho.R
import com.fatec.fatekinho.data_class.TableUserDetails

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ROW = 1
class TableUserDetailsAdapter(private val items: List<TableUserDetails>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class headerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val headercol1: TextView = itemView.findViewById(R.id.users_details_header_col1)
        val headercol2: TextView = itemView.findViewById(R.id.users_details_header_col2)
        val headercol3: TextView = itemView.findViewById(R.id.users_details_header_col3)
    }

    class rowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val rowCol1: TextView = itemView.findViewById(R.id.users_details_col1)
        val rowCol2: TextView = itemView.findViewById(R.id.users_details_col2)
        val rowCol3: TextView = itemView.findViewById(R.id.users_details_col3)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ROW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.header_table_users_details, parent, false)
            headerViewHolder(view)
        } else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table_users_details, parent, false)
            rowViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1 // +1 para o cabeçalho
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is rowViewHolder){
            val item = items[position - 1] // Subtraia 1 por causa do cabeçalho
            holder.rowCol1.text = item.id.toString()
            holder.rowCol2.text = item.tipo
            holder.rowCol3.text = item.data
        }
    }
}