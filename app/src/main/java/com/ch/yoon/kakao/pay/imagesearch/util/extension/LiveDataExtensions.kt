package com.ch.yoon.kakao.pay.imagesearch.util.extension

import androidx.lifecycle.MutableLiveData

/**
 * Creator : ch-yoon
 * Date : 2019-10-25
 **/

inline fun <T> MutableLiveData<T>.updateOnMainThread(block:(T?) -> T?) {
    value = block(value)
}