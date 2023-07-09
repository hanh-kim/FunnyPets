package com.hpk.funnypet.views.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.hpk.funnypet.databinding.ViewDetailImageBinding
import com.hpk.funnypet.model.Photo

class TabControllView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = ViewDetailImageBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
    }

    fun setModel(model: Photo?) {
        model?.let { photo ->
            binding.model = photo
        }
    }
}
