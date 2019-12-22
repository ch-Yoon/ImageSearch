package com.ch.yoon.local.room

import com.ch.yoon.data.source.searchlog.SearchLogLocalDataSource
import com.ch.yoon.local.room.dao.SearchLogDAO
import com.ch.yoon.local.room.model.LocalSearchLog
import com.ch.yoon.local.room.model.mapper.SearchLogEntityMapper
import com.ch.yoon.local.room.transformer.error.CompletableExceptionTransformer
import com.ch.yoon.local.room.transformer.error.SingleExceptionTransformer
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class SearchLogLocalDataSourceImpl(
    private val searchLogDAO: SearchLogDAO
) : SearchLogLocalDataSource {

    override fun insertOrUpdateSearchLog(keyword: String): Single<com.ch.yoon.data.model.searchlog.SearchLog> {
        val newSearchLogEntity = LocalSearchLog(keyword, System.currentTimeMillis())
        return searchLogDAO.insertOrUpdateSearchLog(newSearchLogEntity)
            .toSingle { newSearchLogEntity }
            .map { SearchLogEntityMapper.fromEntity(it) }
            .compose(SingleExceptionTransformer())
    }

    override fun getAllSearchLogs(): Single<List<com.ch.yoon.data.model.searchlog.SearchLog>> {
        return searchLogDAO.selectAllSearchLogs()
            .map { SearchLogEntityMapper.fromEntityList(it) }
            .compose(SingleExceptionTransformer())
    }

    override fun deleteSearchLog(searchLog: com.ch.yoon.data.model.searchlog.SearchLog): Completable {
        return searchLogDAO.deleteSearchLog(searchLog.keyword, searchLog.time)
            .compose(CompletableExceptionTransformer())
    }

    override fun deleteAllSearchLogs(): Completable {
        return searchLogDAO.deleteAllSearchLog()
            .compose(CompletableExceptionTransformer())
    }
}