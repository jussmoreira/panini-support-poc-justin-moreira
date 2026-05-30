package com.panini.support.data.repository

/**
 * Sealed wrapper for network operation outcomes.
 * Allows ViewModels to handle success and error cases exhaustively
 * without exposing raw exceptions to the UI.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val message: String,
        val statusCode: Int? = null
    ) : ApiResult<Nothing>()
}
