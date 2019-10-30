package com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model

import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.entity.SearchLogModel

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
data class SearchLog(
    val keyword: String,
    val time: Long
) : Comparable<SearchLogModel> {

    override fun compareTo(other: SearchLogModel): Int {
        return when {
            time < other.time -> 1
            time > other.time -> -1
            else -> 0
        }
    }
}