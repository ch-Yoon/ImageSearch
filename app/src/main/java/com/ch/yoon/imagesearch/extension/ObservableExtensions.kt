package com.ch.yoon.imagesearch.extension

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Creator : ch-yoon
 * Date : 2019-12-19.
 */
fun <T> Observable<T>.throttleFirstWithHalfSecond(): Observable<T> {
    return throttleFirst(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
}
