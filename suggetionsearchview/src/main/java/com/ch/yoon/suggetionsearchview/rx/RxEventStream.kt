package com.ch.yoon.suggetionsearchview.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Creator : ch-yoon
 * Date : 2019-12-21
 **/
internal open class RxEventStream<T> {

    private val _events = PublishSubject.create<T>()
    val events: Observable<T> = _events

    protected fun publishEvent(event: T) {
        _events.onNext(event)
    }
}