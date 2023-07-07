package com.hpk.funnypet.views.fragments.b

import com.hpk.funnypet.interactor.usecase.GetPhotosUseCase
import com.hpk.funnypet.pagingsource.PhotosPagingSource
import com.hpk.funnypet.views.base.BaseViewModel
import initPagingLiveData

class PhotoDetailViewModel(
    private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel() {

    val photoListLD = initPagingLiveData {
        PhotosPagingSource(getPhotosUseCase)
    }

}