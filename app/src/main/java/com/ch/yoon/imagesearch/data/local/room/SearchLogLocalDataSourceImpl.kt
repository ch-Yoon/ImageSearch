package com.ch.yoon.imagesearch.data.local.room

import com.ch.yoon.imagesearch.data.local.room.dao.SearchLogDAO
import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntity
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogLocalDataSource
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog
import com.ch.yoon.imagesearch.data.local.room.entity.mapper.SearchLogEntityMapper
import com.ch.yoon.imagesearch.data.local.room.transformer.error.CompletableExceptionTransformer
import com.ch.yoon.imagesearch.data.local.room.transformer.error.SingleExceptionTransformer
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class SearchLogLocalDataSourceImpl(
    private val searchLogDAO: SearchLogDAO
) : SearchLogLocalDataSource {

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog> {
        val newSearchLogEntity = SearchLogEntity(keyword, System.currentTimeMillis())
        return searchLogDAO.insertOrUpdateSearchLog(newSearchLogEntity)
            .toSingle { newSearchLogEntity }
            .map { SearchLogEntityMapper.fromEntity(it) }
            .compose(SingleExceptionTransformer())
    }

    override fun getAllSearchLogs(): Single<List<SearchLog>> {
        return searchLogDAO.selectAllSearchLogs()
            .map { SearchLogEntityMapper.fromEntityList(it) }
            .compose(SingleExceptionTransformer())
    }

    override fun deleteSearchLog(searchLog: SearchLog): Completable {
        return searchLogDAO.deleteSearchLog(searchLog.keyword, searchLog.time)
            .compose(CompletableExceptionTransformer())
    }

    override fun deleteAllSearchLog(): Completable {
        return searchLogDAO.deleteAllSearchLog()
            .compose(CompletableExceptionTransformer())
    }
}