package com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.transformer

import io.reactivex.*

class SingleExceptionTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext { throwable ->
            Single.error(RoomExceptionMapper.toRepositoryException(throwable))
        }
    }
}

