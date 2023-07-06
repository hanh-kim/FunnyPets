package com.hpk.funnypet.repository

import com.hpk.funnypet.exception.Failure
import com.hpk.funnypet.interactor.Result
import com.hpk.funnypet.utils.NetworkUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.awaitResponse

abstract class BaseRepository {
    suspend fun <T> request(
        call: Call<T>
    ): Result<Failure, T> {
        if (!NetworkUtil.isNetworkConnected) {
            return Result.Error(Failure.NetworkConnection)
        }
        return try {
            val response = call.awaitResponse()
            val responseBody = response.body()

            when (response.code()) {
                in 200..299 -> {
                    if (responseBody != null) {
                        Result.Success(responseBody)
                    } else {
                        if (response.code() == 204) {
                            Result.Error(Failure.UserNotFound)
                        } else {
                            Result.Error(Failure.DataEmpty)
                        }
                    }
                }
                400 -> {
                    val json = JSONObject(response.errorBody()?.string().toString())
                    val message = json.getString("message")
                    Result.Error(Failure.BadRequestError(message, call.request().url.toString()))
                }
                401 -> {
                    Result.Error(Failure.UnAuthorizedError)
                }
                404 -> {
                    Result.Error(Failure.DataNotFoundError)
                }
                409 -> {
                    Result.Error(Failure.ConflictError)
                }
                500 -> {
                    Result.Error(Failure.InternalServerError)
                }
                else -> {
                    Result.Error(Failure.ServerError)
                }
            }
        } catch (exception: Throwable) {
            Result.Error(Failure.JsonConvertError(exception.message.toString(), call.request().url.toString()))
        }
    }

}