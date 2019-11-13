package com.ch.yoon.imagesearch.data.repository.searchlog.model

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
data class SearchLog(
    val keyword: String,
    val time: Long
) : Comparable<SearchLog> {

    override fun compareTo(other: SearchLog): Int {
        return when {
            time < other.time -> 1
            time > other.time -> -1
            else -> 0
        }
    }
}