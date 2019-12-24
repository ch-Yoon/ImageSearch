package com.ch.yoon.remote.extension

import com.ch.yoon.remote.transformer.error.SingleExceptionTransformer
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-12-24.
 */
internal fun <T> Single<T>.composeDataLayerException(): Single<T> {
    return compose(SingleExceptionTransformer())
}