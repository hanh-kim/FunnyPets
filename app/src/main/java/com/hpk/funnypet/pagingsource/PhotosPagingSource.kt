package com.hpk.funnypet.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hpk.funnypet.interactor.usecase.GetPhotosUseCase
import com.hpk.funnypet.model.Photo
import com.hpk.funnypet.model.PhotosRequest

class PhotosPagingSource(
    private val useCase: GetPhotosUseCase
) : PagingSource<Int, Photo>() {
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val currentKey = params.key ?: 1
        Log.v("kkkkk", "currentPage: $currentKey")
        return useCase.run(
            PhotosRequest(page = currentKey, limit = params.loadSize)
        ).fold(
            onFailure = {
                LoadResult.Error(it)
            },
            onSuccess = {
                LoadResult.Page(
                    data = it.photos?.photo ?: listOf(),
                    prevKey = null,
                    nextKey = (currentKey + 1).takeIf { _ ->
                        (it.photos?.photo?.size ?: 0) == params.loadSize
                    }
                )
            }
        )
    }
}