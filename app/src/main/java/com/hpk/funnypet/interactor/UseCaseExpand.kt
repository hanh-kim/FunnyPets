package com.hpk.funnypet.interactor

import com.hpk.funnypet.exception.Failure
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
abstract class UseCaseExpand<out Type, in Params> where Type : Any {
    abstract suspend fun run(
        params: Params,
        jsonType: JsonType = JsonType.NET
    ): Result<Failure, Type>

    operator fun invoke(
        params: Params,
        jsonType: JsonType = JsonType.NET,
        scope: CoroutineScope = GlobalScope,
        onResult: (Result<Failure, Type>) -> Unit = {}
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                run(params, jsonType)
            }
            onResult(deferred.await())
        }
    }

    class None
    enum class JsonType {
        NET,
        NATIVE
    }
}