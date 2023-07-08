import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.hpk.funnypet.R
import com.hpk.funnypet.utils.Constants
import com.hpk.funnypet.utils.FileUtil
import com.hpk.funnypet.views.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

fun <K : Any, V : Any> BaseViewModel.initPagingLiveData(
    pageSize: Int = Constants.DEFAULT_PAGING_SIZE,
    initialLoadSize: Int = Constants.DEFAULT_PAGING_LIST_INITIAL_PAGE_LOAD_SIZE,
    prefetchDistance: Int = 1,
    pagingSourceFactory: () -> PagingSource<K, V>
): LiveData<PagingData<V>> {
    return Pager(
        PagingConfig(
            pageSize = Constants.DEFAULT_PAGING_SIZE,
            initialLoadSize = Constants.DEFAULT_PAGING_LIST_INITIAL_PAGE_LOAD_SIZE,
            prefetchDistance = prefetchDistance
        ), pagingSourceFactory = pagingSourceFactory
    ).liveData.cachedIn(this)
}

const val CLICK_THROTTLE_DELAY = 800L
fun View.onAvoidDoubleClick(
    throttleDelay: Long = CLICK_THROTTLE_DELAY, onClick: (View) -> Unit
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

fun ImageView.loadImage(img: Any?) {
    Glide.with(this.context).load(img).timeout(5000).placeholder(R.drawable.cell_image_error)
        .error(R.drawable.cell_image_error).into(this)
}

fun ImageView.loadImageUrl(url: String?, listener: () -> Unit = {}, onError: () -> Unit = {}) {
    Glide.with(this.context).load(url).timeout(5000)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
            ): Boolean {
                onError()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                listener()
                return false
            }

        }).placeholder(R.drawable.cell_image_error).error(R.drawable.cell_image_error).into(this)
}

fun Context.shareImage(bitmap: Bitmap) {
    val imageFolder = File(cacheDir, "images")
    var uri: Uri? = null
    try {
        if (imageFolder.exists()) {
            //delete old image
            FileUtil.deleteDir(imageFolder)
        }
        imageFolder.mkdirs()
        val file = File(imageFolder, "funny_pet_share_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        uri = FileProvider.getUriForFile(this, getString(R.string.file_provider), file)
    } catch (e: Exception) {
        println()
    }

    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.putExtra(Intent.EXTRA_TEXT, "")
    intent.putExtra(Intent.EXTRA_SUBJECT, "")
    intent.type = "image/jpeg"
    startActivity(Intent.createChooser(intent, ""))
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.fadeInOut(lifecycleScope : LifecycleCoroutineScope, duration: Long= 1000){
    this.apply {
        visible()
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 1000

        if(duration > 1000){
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.interpolator = AccelerateInterpolator()
            fadeOut.startOffset = duration.minus(1000)
            fadeOut.duration = duration.minus(1000)
        }
        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        this.animation = animation
        animation.start()
        lifecycleScope.launch {
            delay(duration)
            gone()
        }
    }
}


fun Context.getImageBitmap(url: String?, onSuccess: (Bitmap) -> Unit = {}) {
    Glide.with(this)
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
