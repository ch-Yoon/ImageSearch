package com.ch.yoon.imagesearch.extension

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Creator : ch-yoon
 * Date : 2019-12-19.
 */
fun <T> Observable<T>.throttleFirstWith(second: Double): Observable<T> {
    val time = (second * 1000L).toLong()
    return throttleFirst(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
}
