package com.hpk.funnypet.repository

import com.hpk.funnypet.api.MainInterface
import com.hpk.funnypet.exception.Failure
import com.hpk.funnypet.interactor.Result
import com.hpk.funnypet.model.PhotosRequest
import com.hpk.funnypet.model.PhotosResponse

interface MainRepository {
    suspend fun getNetPhotos(params: PhotosRequest): Result<Failure, PhotosResponse>

    class MainRepositoryImpl(private val apiInterface: MainInterface) : MainRepository,
        BaseRepository() {
        override suspend fun getNetPhotos(params: PhotosRequest): Result<Failure, PhotosResponse> {
            return request(apiInterface.getPhotos(page = params.page, limit = params.limit))
        }
    }
}