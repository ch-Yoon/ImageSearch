package com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.transformer

import androidx.room.EmptyResultSetException
import com.ch.yoon.kakao.pay.imagesearch.data.repository.error.RepositoryException

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object RoomExceptionMapper {

    fun toRepositoryException(throwable: Throwable): RepositoryException {
        val errorMessage: String? = throwable.message
        return when(throwable) {
            is EmptyResultSetException -> {
                RepositoryException.NotFoundException(errorMessage ?: "no result error")
            }
            else -> {
                RepositoryException.DatabaseUnknownException(errorMessage ?: "unknown error")
            }
        }
    }
}