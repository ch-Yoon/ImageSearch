package com.ch.yoon.local.room.transformer.error

import io.reactivex.*

class SingleExceptionTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext { throwable ->
            Single.error(RoomExceptionConverter.toRepositoryException(throwable))
        }
    }
}

