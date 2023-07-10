package com.hpk.funnypet.views.fragments.a

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hpk.funnypet.interactor.usecase.GetPhotosUseCase
import com.hpk.funnypet.model.Photo
import com.hpk.funnypet.pagingsource.PhotosPagingSource
import com.hpk.funnypet.utils.PreferenceUtil
import com.hpk.funnypet.views.base.BaseViewModel
import initPagingLiveData

class PhotoListViewModel(
    private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel() {

    val photoListLD = initPagingLiveData {
        PhotosPagingSource(getPhotosUseCase)
    }

    private val _photoHistoryLD: MutableLiveData<List<Photo>> = MutableLiveData()
    val photoHistoryLD: LiveData<List<Photo>> = _photoHistoryLD

    fun getHistory(){
        val photoHistory = PreferenceUtil.photoHistory
        _photoHistoryLD.value = photoHistory
    }

    fun saveToHistory(photo: Photo){
        val photoHistory = PreferenceUtil.photoHistory.toMutableList()
        photoHistory.removeIf { it.id == photo.id }
        photoHistory.add(0, photo)
        PreferenceUtil.photoHistory = photoHistory
        getHistory()
    }

    fun removePhotoFromHistory(photo: Photo){
        val photoHistory = PreferenceUtil.photoHistory.toMutableList()
        photoHistory.removeIf { it.id == photo.id }
        PreferenceUtil.photoHistory = photoHistory
        getHistory()
    }
}