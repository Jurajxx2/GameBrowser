package com.juraj.gamebrowser.shared.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.juraj.gamebrowser.domain.error.AppError
import com.juraj.gamebrowser.shared.R

@Composable
fun Throwable.toUserMessage(): String = when (this) {
    is AppError.NoInternet -> stringResource(R.string.error_no_internet)
    is AppError.ServerError -> stringResource(R.string.error_server)
    is AppError.Unknown -> stringResource(R.string.error_unknown)
    else -> stringResource(R.string.error_unknown)
}
