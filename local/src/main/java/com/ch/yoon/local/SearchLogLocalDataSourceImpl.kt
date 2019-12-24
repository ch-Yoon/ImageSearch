package com.ch.yoon.local

import com.ch.yoon.data.model.searchlog.SearchLogEntity
import com.ch.yoon.data.source.searchlog.SearchLogLocalDataSource
import com.ch.yoon.local.dao.SearchLogDAO
import com.ch.yoon.local.extension.composeDataLayerException
import com.ch.yoon.local.model.searchlog.LocalSearchLog
import com.ch.yoon.local.mapper.searchlog.SearchLogEntityMapper
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class SearchLogLocalDataSourceImpl(
    private val searchLogDAO: SearchLogDAO
) : SearchLogLocalDataSource {

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLogEntity> {
        val newSearchLogEntity = LocalSearchLog(keyword, System.currentTimeMillis())
        return searchLogDAO.insertOrUpdateSearchLog(newSearchLogEntity)
            .toSingle { newSearchLogEntity }
            .map { SearchLogEntityMapper.fromLocal(it) }
            .composeDataLayerException()
    }

    override fun getAllSearchLogs(): Single<List<SearchLogEntity>> {
        return searchLogDAO.selectAllSearchLogs()
            .map { SearchLogEntityMapper.fromLocal(it) }
            .composeDataLayerException()
    }

    override fun deleteSearchLog(searchLogEntity: SearchLogEntity): Completable {
        return searchLogDAO.deleteSearchLog(searchLogEntity.keyword, searchLogEntity.time)
            .composeDataLayerException()
    }

    override fun deleteAllSearchLogs(): Completable {
        return searchLogDAO.deleteAllSearchLog()
            .composeDataLayerException()
    }
}