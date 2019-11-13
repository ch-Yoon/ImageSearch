package com.ch.yoon.imagesearch.data.local.room.error

import com.ch.yoon.imagesearch.data.local.room.error.mapper.RoomExceptionMapper
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer

class CompletableExceptionTransformer : CompletableTransformer {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.onErrorResumeNext { throwable ->
            Completable.error(RoomExceptionMapper.toRepositoryException(throwable))
        }
    }
}