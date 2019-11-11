package com.ch.yoon.suggetionsearchview.model

/**
 * Creator : ch-yoon
 * Date : 2019-11-12
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