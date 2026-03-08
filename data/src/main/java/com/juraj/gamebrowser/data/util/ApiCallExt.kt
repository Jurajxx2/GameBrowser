package com.juraj.gamebrowser.data.util

import android.util.Log
import androidx.paging.PagingSource.LoadResult
import com.juraj.gamebrowser.domain.error.AppError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: IOException) {
    Log.e("safeApiCall", "safeApiCall: IOException", e)
    Result.failure(AppError.NoInternet)
} catch (e: ClientRequestException) {
    Log.e("safeApiCall", "safeApiCall: ClientRequestException", e)
    Result.failure(AppError.ServerError(e.response.status.value))
} catch (e: ServerResponseException) {
    Log.e("safeApiCall", "safeApiCall: ServerResponseException", e)
    Result.failure(AppError.ServerError(e.response.status.value))
} catch (e: Exception) {
    Log.e("safeApiCall", "safeApiCall: Exception", e)
    Result.failure(AppError.Unknown)
}

suspend fun <K : Any, V : Any> safePagingLoad(
    block: suspend () -> LoadResult<K, V>
): LoadResult<K, V> = try {
    block()
} catch (e: IOException) {
    Log.e("safePagingLoad", "safePagingLoad: IOException", e)
    LoadResult.Error(AppError.NoInternet)
} catch (e: ClientRequestException) {
    Log.e("safePagingLoad", "safePagingLoad: ClientRequestException", e)
    LoadResult.Error(AppError.ServerError(e.response.status.value))
} catch (e: ServerResponseException) {
    Log.e("safePagingLoad", "safePagingLoad: ServerResponseException", e)
    LoadResult.Error(AppError.ServerError(e.response.status.value))
} catch (e: Exception) {
    Log.e("safePagingLoad", "safePagingLoad: Exception", e)
    LoadResult.Error(AppError.Unknown)
}
