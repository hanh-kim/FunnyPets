package com.hpk.funnypet.views.others

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import loadImageUrl

@BindingAdapter("src_image_url")
fun loadImageUrlBinding(imageView: ImageView, url: String?) {
    imageView.loadImageUrl(url)
}

@BindingAdapter("src_bitmap")
fun setBitmap(imageView: ImageView, bitmap: Bitmap?) {
    imageView.setImageBitmap(bitmap)
}
