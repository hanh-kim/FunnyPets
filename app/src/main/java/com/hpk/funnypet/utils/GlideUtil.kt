package com.hpk.funnypet.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.io.File

object GlideUtil {

    fun getImageBitmap(context: Context, url: String?, onSuccess: (Bitmap) -> Unit){
        Glide.with(context)
            .asBitmap()
            .load(url)
            .timeout(5000)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap, transition: Transition<in Bitmap?>?
                ) {
                    onSuccess.invoke(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    fun getFileFromGifUrl(context: Context, url: String?, onSuccess: (File) -> Unit){
        Glide.with(context)
            .download(url)
            .listener(object: RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: File?,
                    model: Any?,
                    target: Target<File>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let { file ->
                        onSuccess.invoke(file)
                    }
                    return false
                }
            })
            .submit()
    }
}