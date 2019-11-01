package com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.error

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