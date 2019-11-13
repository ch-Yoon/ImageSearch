package com.ch.yoon.imagesearch.data.remote.kakao.error

import com.ch.yoon.imagesearch.data.remote.kakao.error.mapper.KakaoSearchExceptionMapper
import io.reactivex.*

class SingleExceptionTransformer<T> : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.onErrorResumeNext { throwable ->
            Single.error(KakaoSearchExceptionMapper.toRepositoryException(throwable))
        }
    }
}

