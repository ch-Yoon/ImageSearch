package com.ch.yoon.kakao.pay.imagesearch.extention

/**
 * Creator : ch-yoon
 * Date : 2019-10-25.
 */

inline fun <T> MutableList<T>.removeFirstIf(predicate: (T) -> Boolean): MutableList<T> {
    for(i in 0 until this.size) {
        if(predicate(this[i])) {
            removeAt(i)
            break
        }
    }

    return this
}

fun <T> MutableList<T>.addFirst(value: T): MutableList<T> {
    add(0, value)

    return this
}