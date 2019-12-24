package com.ch.yoon.data.model.searchlog

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
data class SearchLogEntity(
    val keyword: String,
    val time: Long
) : Comparable<SearchLogEntity> {

    override fun compareTo(other: SearchLogEntity): Int {
        return when {
            time < other.time -> 1
            time > other.time -> -1
            else -> 0
        }
    }
}