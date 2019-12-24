package com.ch.yoon.remote.transformer.error

import io.reactivex.*

class SingleExceptionTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext { throwable ->
            Single.error(KakaoExceptionConverter.toRepositoryException(throwable))
        }
    }

}