package com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Entity(tableName = "searchLogs")
data class SearchLog(
    @PrimaryKey val keyword: String,
    val time: Long
) :  Comparable<SearchLog> {

    override fun compareTo(other: SearchLog): Int {
        return when {
            time < other.time -> 1
            time > other.time -> -1
            else -> 0
        }
    }

}