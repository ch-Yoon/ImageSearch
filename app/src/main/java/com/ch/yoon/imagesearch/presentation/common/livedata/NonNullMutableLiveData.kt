package com.ch.yoon.imagesearch.presentation.common.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
class NonNullMutableLiveData<T>(private val defaultValue: T): MutableLiveData<T>(defaultValue) {

    override fun setValue(value: T) {
        super.setValue(value)
    }

    override fun postValue(value: T) {
        super.postValue(value)
    }

    override fun getValue(): T {
        return super.getValue() ?: defaultValue
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer<T> { value ->
            observer.onChanged(value ?: defaultValue)
        })
    }
}