package com.example.kurspredict.ui.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kurspredict.R

class ModelFragment : Fragment() {

    private lateinit var tvArsitektur: TextView
    private lateinit var iconArsitektur: ImageView
    private var isArsitekturExpanded = false

    private lateinit var layoutPreprocessingContent: LinearLayout
    private lateinit var iconPreprocessing: ImageView
    private var isPreprocessingExpanded = false

    private lateinit var layoutModelingContent: LinearLayout
    private lateinit var iconModeling: ImageView
    private var isModelingExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_model, container, false)

        // === Arsitektur Model Expand ===
        tvArsitektur = view.findViewById(R.id.tvArsitektur)
        iconArsitektur = view.findViewById(R.id.iconArsitektur)
        val sectionArsitektur = view.findViewById<LinearLayout>(R.id.sectionArsitektur)

        sectionArsitektur.setOnClickListener {
            isArsitekturExpanded = !isArsitekturExpanded
            tvArsitektur.visibility = if (isArsitekturExpanded) View.VISIBLE else View.GONE
            iconArsitektur.setImageResource(
                if (isArsitekturExpanded)
                    R.drawable.baseline_arrow_drop_up_24
                else
                    R.drawable.baseline_arrow_drop_down_24
            )
        }

        // === Preprocessing Expand ===
        layoutPreprocessingContent = view.findViewById(R.id.layoutPreprocessingContent)
        iconPreprocessing = view.findViewById(R.id.iconPreprocessing)
        val sectionPreprocessing = view.findViewById<LinearLayout>(R.id.sectionPreprocessing)

        sectionPreprocessing.setOnClickListener {
            isPreprocessingExpanded = !isPreprocessingExpanded
            layoutPreprocessingContent.visibility =
                if (isPreprocessingExpanded) View.VISIBLE else View.GONE
            iconPreprocessing.setImageResource(
                if (isPreprocessingExpanded)
                    R.drawable.baseline_arrow_drop_up_24
                else
                    R.drawable.baseline_arrow_drop_down_24
            )
        }

        // === Modeling Expand ===
        layoutModelingContent = view.findViewById(R.id.layoutModelingContent)
        iconModeling = view.findViewById(R.id.iconModeling)
        val sectionModeling = view.findViewById<LinearLayout>(R.id.sectionModeling)

        sectionModeling.setOnClickListener {
            isModelingExpanded = !isModelingExpanded
            layoutModelingContent.visibility =
                if (isModelingExpanded) View.VISIBLE else View.GONE
            iconModeling.setImageResource(
                if (isModelingExpanded)
                    R.drawable.baseline_arrow_drop_up_24
                else
                    R.drawable.baseline_arrow_drop_down_24
            )
        }

        return view
    }
}
