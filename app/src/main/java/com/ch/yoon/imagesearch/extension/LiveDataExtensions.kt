package com.ch.yoon.imagesearch.extension

import androidx.lifecycle.MutableLiveData

/**
 * Creator : ch-yoon
 * Date : 2019-10-25
 **/
inline fun <T> MutableLiveData<MutableList<T>>.removeFirstAndAddFirst(insertValue: T, predicate: (T) -> Boolean) {
    value?.let { list ->
        list.removeFirstIf(predicate)
        list.add(0, insertValue)
        value = list
    }
}

inline fun <T> MutableLiveData<MutableList<T>>.removeFirst(predicate: (T) -> Boolean) {
    value?.let { list ->
        list.removeFirstIf(predicate)
        value = list
    }
}


inline fun <T> MutableLiveData<MutableList<T>>.replace(replaceValue: T, predicate: (T) -> Boolean) {
    value?.let { list ->
        list.replace(replaceValue, predicate)
        value = list
    }
}

inline fun <T> MutableLiveData<MutableList<T>>.find(predicate: (T) -> Boolean): T? {
    return value?.let { list ->
        list.find { predicate(it) }
    }
}

fun <T> MutableLiveData<MutableList<T>>.clear() {
    value?.let { list ->
        list.clear()
        value = list
    }
}

fun <T> MutableLiveData<MutableList<T>>.isNotEmpty(): Boolean {
    return isEmpty().not()
}

fun <T> MutableLiveData<MutableList<T>>.isEmpty(): Boolean {
    return value?.isEmpty() ?: true
}

fun <T> MutableLiveData<MutableList<T>>.addAll(list: List<T>) {
    val newList = value ?: mutableListOf()
    newList.addAll(list)
    value = newList
}

fun <T> MutableLiveData<MutableList<T>>.add(t: T) {
    val newList = value ?: mutableListOf()
    newList.add(t)
    value = newList
}

fun <T> MutableLiveData<MutableList<T>>.size(): Int {
    return value?.size ?: 0
}