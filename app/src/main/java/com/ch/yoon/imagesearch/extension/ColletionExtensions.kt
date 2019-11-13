package com.ch.yoon.imagesearch.extension

/**
 * Creator : ch-yoon
 * Date : 2019-10-25.
 */

inline fun <T> MutableList<T>.removeFirstIf(predicate: (T) -> Boolean): MutableList<T> {
    for(i in 0 until size) {
        if(predicate(this[i])) {
            removeAt(i)
            break
        }
    }

    return this
}

inline fun <T> MutableList<T>.replace(replaceValue: T, predicate: (T) -> Boolean) {
    for(i in 0 until size) {
        if(predicate(this[i])) {
            this[i] = replaceValue
            break
        }
    }
}

fun <T> MutableList<T>.addFirst(value: T): MutableList<T> {
    add(0, value)

    return this
}