package com.ch.yoon.local.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Entity(tableName = "searchLogs")
data class LocalSearchLog(
    @PrimaryKey val keyword: String,
    val time: Long
)