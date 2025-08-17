package org.purboyndradev.rt_rw.core.domain

sealed interface Result<out S, out E : AppError> {
    data class Success<out S>(val data: S) : Result<S, Nothing>
    data class Error<out E : AppError>(val error: E) :
        Result<Nothing, E>
}

inline fun <T, E : AppError, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(transform((data)))
    }
}


inline fun <T, E : AppError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : AppError> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        
        is Result.Success -> Result.Success(data)
    }
}

inline fun <S, E : AppError, R, E2 : AppError> Result<S, E>.mapBoth(
    onSuccess: (S) -> Result<R, E2>,
    onFailure: (E) -> Result<R, E2>
): Result<R, E2> {
    return when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Error -> onFailure(error)
    }
}


typealias EmptyResult<E> = Result<Unit, E>