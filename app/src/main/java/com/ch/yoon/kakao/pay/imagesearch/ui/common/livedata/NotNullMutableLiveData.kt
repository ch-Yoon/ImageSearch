package com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Creator : ch-yoon
 * Date : 2019-10-28.
 */
class NotNullMutableLiveData<T>(private val defaultValue: T) : MutableLiveData<T>() {

    init {
        value = defaultValue
    }

    override fun getValue(): T {
        return super.getValue() ?: defaultValue
    }

}