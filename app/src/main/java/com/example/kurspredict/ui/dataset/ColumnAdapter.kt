package com.example.kurspredict.ui.dataset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kurspredict.R

class ColumnAdapter(
    private val headers: List<String>,
    private val columns: List<List<String>>,
) : RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder>() {

    inner class ColumnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerText: TextView = itemView.findViewById(R.id.tvHeader)
        val valuesText: TextView = itemView.findViewById(R.id.tvValues)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_column, parent, false)
        return ColumnViewHolder(view)
    }

    override fun getItemCount(): Int = headers.size

    override fun onBindViewHolder(holder: ColumnViewHolder, position: Int) {
        holder.headerText.text = headers[position]
        holder.valuesText.text = columns[position].joinToString("\n")
    }
}

