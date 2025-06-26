package com.example.kurspredict.ui.prediksi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kurspredict.databinding.ItemPredictionBinding
import com.example.kurspredict.model.PredictionData
import java.text.NumberFormat
import java.util.*

class PredictionAdapter(private val data: List<PredictionData>) :
    RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder>() {

    inner class PredictionViewHolder(private val binding: ItemPredictionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PredictionData) {
            binding.tvTanggal.text = item.tanggal
            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            binding.tvNilai.text = formatter.format(item.nilai)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPredictionBinding.inflate(inflater, parent, false)
        return PredictionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}
