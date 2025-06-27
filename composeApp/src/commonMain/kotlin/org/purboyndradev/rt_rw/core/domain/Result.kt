package org.purboyndradev.rt_rw.core.domain

sealed interface Result<out S, out E : Error> {
    data class Success<out S>(val data: S) : Result<S, Nothing>
    data class Error<out E : org.purboyndradev.rt_rw.core.domain.Error>(val error: E) :
        Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(transform((data)))
    }
}

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}


inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        
        is Result.Success -> Result.Success(data)
    }
}


typealias EmptyResult<E> = Result<Unit, E>