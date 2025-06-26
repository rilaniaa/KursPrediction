package com.example.kurspredict.ui.prediksi

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurspredict.databinding.FragmentPrediksiBinding
import com.example.kurspredict.model.PredictionData
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.data.*
import org.tensorflow.lite.Interpreter
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PrediksiFragment : Fragment() {
    private var _binding: FragmentPrediksiBinding? = null
    private val binding get() = _binding!!
    private lateinit var tflite: Interpreter
    private lateinit var fullData: List<Float>

    // Min dan Max yang digunakan saat normalisasi training
    private val minValue = 14000f
    private val maxValue = 17000f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPrediksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fullData = loadData("prediksi_kurs.csv")
        tflite = loadModel("idr.tflite")

        val predictedData = predictKurs(fullData.takeLast(60))
        val tanggalPrediksi = generateFutureDates("2025-06-23", predictedData.size)

        val allData = tanggalPrediksi.zip(predictedData).map { (tanggal, nilai) ->
            PredictionData(tanggal, nilai)
        }

        binding.seekBar.max = allData.size
        binding.seekBar.progress = 30
        binding.selectedDayText.text = "Jumlah Hari: 30"

        updateUI(allData, 30)

        binding.seekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                binding.selectedDayText.text = "Jumlah Hari: $progress"
                updateUI(allData, progress)
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
    }

    private fun updateUI(data: List<PredictionData>, days: Int) {
        val subData = data.take(days)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = PredictionAdapter(subData)

        val entries = subData.mapIndexed { index, item -> Entry(index.toFloat(), item.nilai) }
        val dataSet = LineDataSet(entries, "Prediksi Kurs").apply {
            color = resources.getColor(android.R.color.holo_blue_dark, null)
            valueTextSize = 10f
            setDrawCircles(false)
        }
        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(subData.map { it.tanggal })
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            labelRotationAngle = -45f
            setDrawGridLines(false)
        }
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.description.isEnabled = false
        binding.lineChart.invalidate()
    }

    private fun loadData(fileName: String): List<Float> {
        val list = mutableListOf<Float>()
        val reader = BufferedReader(InputStreamReader(requireContext().assets.open(fileName)))
        reader.readLine() // skip header
        reader.forEachLine { line ->
            val nilai = line.split(",").getOrNull(1)?.toFloatOrNull()
            if (nilai != null) list.add(nilai)
        }
        return list
    }

    private fun loadModel(modelFile: String): Interpreter {
        val modelStream = requireContext().assets.open(modelFile)
        val modelBytes = modelStream.readBytes()
        val buffer = ByteBuffer.allocateDirect(modelBytes.size).apply {
            order(ByteOrder.nativeOrder())
            put(modelBytes)
            rewind()
        }
        return Interpreter(buffer)
    }

    private fun predictKurs(last60: List<Float>): List<Float> {
        val input = Array(1) { Array(60) { i -> FloatArray(1) { last60[i] } } }
        val output = Array(1) { FloatArray(180) }

        tflite.run(input, output)

        // Denormalisasi hasil prediksi
        return output[0].map { it * (maxValue - minValue) + minValue }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateFutureDates(startDate: String, days: Int): List<String> {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val baseDate = java.time.LocalDate.parse(startDate, formatter)
        return List(days) { baseDate.plusDays(it.toLong()).format(formatter) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
