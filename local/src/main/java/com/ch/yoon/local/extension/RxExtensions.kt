package com.ch.yoon.local.extension

import com.ch.yoon.local.transformer.error.CompletableExceptionTransformer
import com.ch.yoon.local.transformer.error.SingleExceptionTransformer
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-12-24.
 */
fun <T> Single<T>.composeDataLayerException(): Single<T> {
    return compose(SingleExceptionTransformer())
}

fun Completable.composeDataLayerException(): Completable {
    return compose(CompletableExceptionTransformer())
}