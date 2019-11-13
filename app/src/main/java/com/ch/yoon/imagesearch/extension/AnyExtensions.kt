package com.ch.yoon.imagesearch.extension

/**
 * Creator : ch-yoon
 * Date : 2019-10-25
 **/

val Any.TAG: String
    get() {
        return javaClass.simpleName
    }