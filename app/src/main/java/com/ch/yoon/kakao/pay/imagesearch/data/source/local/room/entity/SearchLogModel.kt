package com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Entity(tableName = "searchLogs")
data class SearchLogModel(
    @PrimaryKey val keyword: String,
    val time: Long
)