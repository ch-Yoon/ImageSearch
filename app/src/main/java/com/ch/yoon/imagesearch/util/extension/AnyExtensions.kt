package com.ch.yoon.imagesearch.util.extension

/**
 * Creator : ch-yoon
 * Date : 2019-10-25
 **/

val Any.TAG: String
    get() {
        return javaClass.simpleName
    }