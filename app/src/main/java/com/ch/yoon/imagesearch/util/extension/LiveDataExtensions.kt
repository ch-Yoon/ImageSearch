package com.ch.yoon.imagesearch.util.extension

import androidx.lifecycle.MutableLiveData

/**
 * Creator : ch-yoon
 * Date : 2019-10-25
 **/
inline fun <T> MutableLiveData<MutableList<T>>.removeFirstAndAddFirst(insertValue: T, predicate: (T) -> Boolean) {
    value?.let { list ->
        for(i in 0 until list.size) {
            if(predicate(list[i])) {
                list.removeAt(i)
                break
            }
        }
        list.add(0, insertValue)
        value = list
    }
}

inline fun <T> MutableLiveData<MutableList<T>>.removeFirst(predicate: (T) -> Boolean) {
    value?.let { list ->
        for(i in 0 until list.size) {
            if(predicate(list[i])) {
                list.removeAt(i)
                break
            }
        }
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

fun <T> MutableLiveData<MutableList<T>>.isEmpty(): Boolean {
    return value?.isEmpty() ?: true
}

fun <T> MutableLiveData<MutableList<T>>.addAll(list: List<T>) {
    val newList = value ?: mutableListOf()
    newList.addAll(list)
    value = newList
}