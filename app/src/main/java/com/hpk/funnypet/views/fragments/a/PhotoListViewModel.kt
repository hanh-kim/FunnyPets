package com.hpk.funnypet.views.fragments.a

import com.hpk.funnypet.interactor.usecase.GetPhotosUseCase
import com.hpk.funnypet.pagingsource.PhotosPagingSource
import com.hpk.funnypet.views.base.BaseViewModel
import initPagingLiveData

class PhotoListViewModel(
    private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel() {

    val photoListLD = initPagingLiveData {
        PhotosPagingSource(getPhotosUseCase)
    }

}