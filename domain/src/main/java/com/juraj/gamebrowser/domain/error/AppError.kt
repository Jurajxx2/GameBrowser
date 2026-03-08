package com.juraj.gamebrowser.domain.error

sealed class AppError : Exception() {
    data object NoInternet : AppError()
    data class ServerError(val code: Int) : AppError()
    data object Unknown : AppError()
}
