package com.example.kurspredict.ui.dataset

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurspredict.R
import com.example.kurspredict.databinding.FragmentDatasetBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class DatasetFragment : Fragment() {

    private lateinit var binding: FragmentDatasetBinding
    private lateinit var csvHeaders: List<String>
    private lateinit var csvColumns: List<List<String>>
    private var isDeskripsiVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatasetBinding.inflate(inflater, container, false)
        loadCSV()

        // Set RecyclerView untuk menampilkan dataset
        val adapter = ColumnAdapter(csvHeaders, csvColumns)
        binding.rvDataset.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDataset.adapter = adapter

        // Expand/Collapse Deskripsi
        binding.sectionDeskripsi.setOnClickListener {
            isDeskripsiVisible = !isDeskripsiVisible

            binding.tvDeskripsi.visibility = if (isDeskripsiVisible) View.VISIBLE else View.GONE
            binding.linkYahoo.visibility = if (isDeskripsiVisible) View.VISIBLE else View.GONE

            binding.iconDeskripsi.setImageResource(
                if (isDeskripsiVisible)
                    R.drawable.baseline_arrow_drop_up_24
                else
                    R.drawable.baseline_arrow_drop_down_24
            )
        }

        binding.linkYahoo.setOnClickListener {
            val url = "https://finance.yahoo.com/quote/IDR%3DX/history/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        return binding.root
    }

    private fun loadCSV() {
        val inputStream = requireContext().assets.open("idrx.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        val lines = reader.readLines()
        if (lines.isEmpty()) return

        csvHeaders = lines[0].split(",")
        val rows = lines.drop(1).take(20)

        csvColumns = List(csvHeaders.size) { mutableListOf<String>() }

        for (row in rows) {
            if (row.isBlank()) continue
            val values = row.split(",")
            for (i in csvHeaders.indices) {
                if (i < values.size) {
                    (csvColumns[i] as MutableList<String>).add(values[i])
                } else {
                    (csvColumns[i] as MutableList<String>).add("")
                }
            }
        }
    }
}
