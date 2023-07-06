package com.hpk.funnypet.repository

import com.hpk.funnypet.api.MainInterface
import com.hpk.funnypet.exception.Failure
import com.hpk.funnypet.interactor.Result

interface MainRepository {
    suspend fun getNetMain(params: Any): Result<Failure, Any>

    class MainRepositoryImpl(private val apiInterface: MainInterface) : MainRepository,
        BaseRepository() {
        override suspend fun getNetMain(params: Any): Result<Failure, Any> {
            return request(apiInterface.getMain())
        }
    }
}