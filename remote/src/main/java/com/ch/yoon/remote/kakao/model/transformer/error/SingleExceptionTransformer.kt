package com.ch.yoon.remote.kakao.model.transformer.error

import io.reactivex.*

class SingleExceptionTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext { throwable ->
            Single.error(KakaoSearchExceptionConverter.toRepositoryException(throwable))
        }
    }
}