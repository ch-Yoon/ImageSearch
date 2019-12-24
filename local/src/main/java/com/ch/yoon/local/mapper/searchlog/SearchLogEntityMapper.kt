package com.ch.yoon.local.mapper.searchlog

import com.ch.yoon.data.model.searchlog.SearchLogEntity
import com.ch.yoon.local.mapper.EntityMapper
import com.ch.yoon.local.model.searchlog.LocalSearchLog

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object SearchLogEntityMapper : EntityMapper<LocalSearchLog, SearchLogEntity> {

    override fun fromLocal(model: LocalSearchLog): SearchLogEntity {
        return model.run { SearchLogEntity(keyword, time) }
    }

    override fun fromLocal(models: List<LocalSearchLog>): List<SearchLogEntity> {
        return models.map { fromLocal(it) }
    }

    override fun toLocal(model: SearchLogEntity): LocalSearchLog {
        return model.run { LocalSearchLog(keyword, time) }
    }

    override fun toLocal(models: List<SearchLogEntity>): List<LocalSearchLog> {
        return models.map { toLocal(it) }
    }
}