package com.ch.yoon.data.repository

import com.ch.yoon.data.source.searchlog.SearchLogLocalDataSource
import com.ch.yoon.domain.model.searchlog.SearchLog
import com.ch.yoon.domain.repository.SearchLogRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Creator : ch-yoon
 * Date : 2019-11-12
 **/
class SearchLogRepositoryImpl(
    private val searchLogLocalDataSource: SearchLogLocalDataSource
) : SearchLogRepository {

    override fun getAllSearchLogs(): Single<List<SearchLog>> {
        return searchLogLocalDataSource.getAllSearchLogs()
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSearchLog(searchLog: SearchLog): Completable {
        return searchLogLocalDataSource.deleteSearchLog(searchLog)
            .subscribeOn(Schedulers.io())
    }

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog> {
        return searchLogLocalDataSource.insertOrUpdateSearchLog(keyword)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteAllSearchLogs(): Completable {
        return searchLogLocalDataSource.deleteAllSearchLogs()
            .subscribeOn(Schedulers.io())
    }

}