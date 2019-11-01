package com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.error

import io.reactivex.*

class SingleExceptionTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext { throwable ->
            Single.error(KakaoSearchExceptionMapper.toRepositoryException(throwable))
        }
    }
}

