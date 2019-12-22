package com.ch.yoon.local.room.model.mapper

import com.ch.yoon.local.room.model.LocalSearchLog

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object SearchLogEntityMapper {

    fun fromEntity(localSearchLog: LocalSearchLog): com.ch.yoon.data.model.searchlog.SearchLog = localSearchLog.run { com.ch.yoon.data.model.searchlog.SearchLog(keyword, time) }

    fun fromEntityList(localSearchLogList: List<LocalSearchLog>) = localSearchLogList.map { com.ch.yoon.data.model.searchlog.SearchLog(it.keyword, it.time) }

}