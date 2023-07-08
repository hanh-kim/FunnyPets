package com.hpk.funnypet.views.fragments.b

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hpk.funnypet.AndroidApplication
import com.hpk.funnypet.interactor.usecase.GetPhotosUseCase
import com.hpk.funnypet.model.Photo
import com.hpk.funnypet.pagingsource.PhotosPagingSource
import com.hpk.funnypet.utils.FileUtil
import com.hpk.funnypet.views.base.BaseViewModel
import initPagingLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class PhotoDetailViewModel(
    private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel() {

    var photo: Photo?= null

    val photoListLD = initPagingLiveData {
        PhotosPagingSource(getPhotosUseCase)
    }


    private val _saveImage: MutableLiveData<Boolean> = MutableLiveData()
    var saveImage : MutableLiveData<Boolean> = _saveImage

    fun saveImage(bitmap: Bitmap, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveBitmapToLocalStorage(bitmap, name)
        }
    }

    @Throws(IOException::class)
    private suspend fun saveBitmapToLocalStorage(bitmap: Bitmap, name: String) {
        withContext(Dispatchers.IO) {
            val savedPath = FileUtil.saveBitmapToDownloads(bitmap, name)
            MediaScannerConnection.scanFile(AndroidApplication.mInstance, arrayOf(savedPath), arrayOf("image/*")) { _, _ ->
                _saveImage.postValue(!TextUtils.isEmpty(savedPath))
            }
        }
    }


}