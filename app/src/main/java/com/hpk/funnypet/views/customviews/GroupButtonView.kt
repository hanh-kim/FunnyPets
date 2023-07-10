package com.hpk.funnypet.views.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.hpk.funnypet.R
import com.hpk.funnypet.databinding.ViewGroupButtonBinding
import onAvoidDoubleClick

class GroupButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = ViewGroupButtonBinding.inflate(LayoutInflater.from(context), this, false)

    var onSelectChanged: (Int) -> Unit = {}
    private var currentSelected = 0

    init {
        addView(binding.root)

        binding.btnPhotos.setOnClickListener {
            if (currentSelected != 0) {
                currentSelected = 0
                changeButtonBackground()
                onSelectChanged.invoke(currentSelected)
            }
        }

        binding.btnHistory.setOnClickListener {
            if (currentSelected != 1) {
                currentSelected = 1
                changeButtonBackground()
                onSelectChanged.invoke(currentSelected)
            }
        }
    }

    private fun changeButtonBackground() {
        when (currentSelected) {
            0 -> {
                binding.btnPhotos.setTextColor(context.getColor(R.color.white))
                binding.btnPhotos.background = ContextCompat.getDrawable(context, R.drawable.bg_button_selected)
                binding.btnHistory.setTextColor(context.getColor(R.color.base_gray_06))
                binding.btnHistory.background = ContextCompat.getDrawable(context, R.drawable.bg_button_unselect)
            }

            else -> {
                binding.btnPhotos.setTextColor(context.getColor(R.color.base_gray_06))
                binding.btnPhotos.background = ContextCompat.getDrawable(context, R.drawable.bg_button_unselect)
                binding.btnHistory.setTextColor(context.getColor(R.color.white))
                binding.btnHistory.background = ContextCompat.getDrawable(context, R.drawable.bg_button_selected)
            }
        }
    }
}
