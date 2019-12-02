package com.ch.yoon.imagesearch.data.local.room.transformer.error

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer

class CompletableExceptionTransformer : CompletableTransformer {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.onErrorResumeNext { throwable ->
            Completable.error(RoomExceptionConverter.toRepositoryException(throwable))
        }
    }
}