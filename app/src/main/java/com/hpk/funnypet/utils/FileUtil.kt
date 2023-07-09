package com.hpk.funnypet.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.hpk.funnypet.AndroidApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    fun saveBitmapToDownloads(bitmap: Bitmap, name: String): String {
        var savedPath = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, name)
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "image/*")
            contentValues.put(MediaStore.Downloads.IS_PENDING, true)
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, "Download/")
            val uri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val context = AndroidApplication.mInstance
            val mContentResolver = context.contentResolver
            val itemUri = mContentResolver.insert(uri, contentValues)
            if (itemUri != null) {
                try {
                    val outputStream = mContentResolver.openOutputStream(itemUri)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream?.flush()
                    outputStream?.close()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, false)
                    mContentResolver.update(itemUri, contentValues, null, null)
                    itemUri.path?.let {
                        savedPath = File(it).absolutePath
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ).toString() + File.separator + Constants.IMAGE_IDENTIFY_PATH

            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            val imageFile = File(imagesDir, name)
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            savedPath = imageFile.absolutePath
        }

        return savedPath
    }

    @SuppressLint("SimpleDateFormat")
    fun createFileFromBitmap(bitmap: Bitmap, fileName: String): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return try {
            val fileDir: File =
                AndroidApplication.mInstance.getExternalFilesDir("image") ?: return null
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            val imageFile = File.createTempFile("JPEG_${timeStamp}_$fileName", ".jpg", fileDir)
            val os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            imageFile
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getFileFromImageUrl(imageUrl: String, fileName: String): File? =
        withContext(Dispatchers.IO) {
            val deferredBitmap = async {
                getBitmapFromImageUrl(imageUrl)
            }
            val bitmap = deferredBitmap.await()
            bitmap?.let {
                createFileFromBitmap(bitmap, fileName)
            } ?: kotlin.run {
                null
            }
        }

    suspend fun getBitmapFromImageUrl(imgUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        if (!NetworkUtil.isNetworkConnected) return@withContext null
        try {
            val connection = URL(imgUrl).openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            return@withContext BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            return@withContext null
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            if (children != null) {
                for (i in children.indices) {
                    deleteDir(File(dir, children[i]))
                }
            }
            dir.deleteRecursively()
        } else if (dir != null && dir.isFile) {
            dir.deleteRecursively()
        } else {
            false
        }
    }

    fun downloadFile(url: String, fileName: String, desc: String) {
        // fileName -> fileName with extension
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription(desc)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager =
            AndroidApplication.mInstance.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        downloadManager?.enqueue(request)
    }
}