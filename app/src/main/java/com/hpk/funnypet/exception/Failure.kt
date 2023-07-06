package com.hpk.funnypet.exception

import com.hpk.funnypet.AndroidApplication
import com.hpk.funnypet.R


/**
 * エラー、failure, exceptionを管理する
 */
@Suppress("unused")
sealed class Failure: Throwable() {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object InternalServerError: Failure()
    class JsonConvertError(val errorMessage: String, val url: String): Failure()
    object UnAuthorizedError : Failure()
    object DataNotFoundError : Failure()
    object DataEmpty : Failure()
    object UserNotFound : Failure()
    object Retry : Failure()
    class BadRequestError(val errorMessage: String, val url: String): Failure()
    class Unknown(val errorMessage: String = "error"): Failure()
    object ConflictError: Failure()
    object ConvertError : Failure()

    //追加エラー処理はこれをExtendして使う
    abstract class FeatureFailure : Failure()
}