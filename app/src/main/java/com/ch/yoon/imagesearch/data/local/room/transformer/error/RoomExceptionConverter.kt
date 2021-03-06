package com.ch.yoon.imagesearch.data.local.room.transformer.error

import androidx.room.EmptyResultSetException
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object RoomExceptionConverter {

    fun toRepositoryException(throwable: Throwable): RepositoryException {
        val errorMessage: String = throwable.message ?: ""
        return when(throwable) {
            is EmptyResultSetException -> {
                RepositoryException.NotFoundException(errorMessage + "no result error")
            }
            else -> {
                RepositoryException.DatabaseUnknownException(errorMessage + "database unknown error")
            }
        }
    }
}