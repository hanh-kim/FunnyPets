package com.hpk.funnypet.interactor.usecase

import com.hpk.funnypet.exception.Failure
import com.hpk.funnypet.interactor.Result
import com.hpk.funnypet.interactor.UseCaseExpand
import com.hpk.funnypet.model.PhotosRequest
import com.hpk.funnypet.model.PhotosResponse
import com.hpk.funnypet.repository.MainRepository

class GetPhotosUseCase(private val mainRepository: MainRepository) : UseCaseExpand<PhotosResponse, PhotosRequest>() {
    override suspend fun run(params: PhotosRequest, jsonType: JsonType): Result<Failure, PhotosResponse> {

        return mainRepository.getNetPhotos(params).fold(
            onSuccess = {
                Result.Success(it)
            },
            onFailure = {
                Result.Error(it)
            }
        )
    }
}