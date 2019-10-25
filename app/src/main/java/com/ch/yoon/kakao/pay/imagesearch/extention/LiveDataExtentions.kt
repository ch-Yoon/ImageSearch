package com.ch.yoon.kakao.pay.imagesearch.extention

import androidx.lifecycle.MutableLiveData

/**
 * Creator : ch-yoon
 * Date : 2019-10-25
 **/

inline fun <T> MutableLiveData<MutableList<T>>.removeFirstAndUpdate(
    isMainThread: Boolean = true,
    predicate: (T) -> Boolean
) {
    value?.let { list ->
        list.removeFirstIf(predicate)
        if(isMainThread) {
            value = list
        } else {
            postValue(list)
        }
    }
}

inline fun <T> MutableLiveData<T>.updateWithSafeValue(
    isMainThread: Boolean = true,
    block: (T) -> T
) {
    value?.let { beforeValue ->
        val afterValue = block(beforeValue)
        if(isMainThread) {
            value = afterValue
        } else {
            postValue(value)
        }
    }
}

inline fun <T> MutableLiveData<T>.updateOnMainThread(block:(T?) -> T?) {
    value = block(value)
}

inline fun <T> MutableLiveData<T>.updateWithUnSafeValue(
    isMainThread: Boolean = true,
    block: (T?) -> T
) {
    value.let { beforeValue ->
        val afterValue = block(beforeValue)
        if(isMainThread) {
            value = afterValue
        } else {
            postValue(value)
        }
    }
}