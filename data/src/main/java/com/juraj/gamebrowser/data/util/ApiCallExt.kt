package com.juraj.gamebrowser.data.util

import androidx.paging.PagingSource.LoadResult
import com.juraj.gamebrowser.domain.error.AppError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import timber.log.Timber
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: IOException) {
    Timber.e(e)
    Result.failure(AppError.NoInternet)
} catch (e: ClientRequestException) {
    Timber.e(e)
    Result.failure(AppError.ServerError(e.response.status.value))
} catch (e: ServerResponseException) {
    Timber.e(e)
    Result.failure(AppError.ServerError(e.response.status.value))
} catch (e: Exception) {
    Timber.e(e)
    Result.failure(AppError.Unknown)
}

suspend fun <K : Any, V : Any> safePagingLoad(
    block: suspend () -> LoadResult<K, V>
): LoadResult<K, V> = try {
    block()
} catch (e: IOException) {
    Timber.e(e)
    LoadResult.Error(AppError.NoInternet)
} catch (e: ClientRequestException) {
    Timber.e(e)
    LoadResult.Error(AppError.ServerError(e.response.status.value))
} catch (e: ServerResponseException) {
    Timber.e(e)
    LoadResult.Error(AppError.ServerError(e.response.status.value))
} catch (e: Exception) {
    Timber.e(e)
    LoadResult.Error(AppError.Unknown)
}
