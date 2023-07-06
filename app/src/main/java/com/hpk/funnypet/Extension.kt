import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hpk.funnypet.R
import com.hpk.funnypet.utils.Constants
import com.hpk.funnypet.utils.FileUtil
import com.hpk.funnypet.views.base.BaseViewModel
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
        ),
        pagingSourceFactory = pagingSourceFactory
    ).liveData.cachedIn(this)
}

const val CLICK_THROTTLE_DELAY = 800L
fun View.onAvoidDoubleClick(
    throttleDelay: Long = CLICK_THROTTLE_DELAY,
    onClick: (View) -> Unit
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

fun ImageView.loadImageUrl(url: String?, listener: () -> Unit= {}, onError: () -> Unit= {}) {
    Glide.with(this.context)
        .load(url)
        .timeout(5000)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
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

        })
        .placeholder(R.drawable.cell_image_error)
        .error(R.drawable.cell_image_error)
        .into(this)
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
//        uri = FileProvider.getUriForFile(this, getString(R.string.file_provider), file)
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
