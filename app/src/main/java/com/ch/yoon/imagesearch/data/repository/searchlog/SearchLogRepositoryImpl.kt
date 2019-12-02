package com.ch.yoon.imagesearch.data.repository.searchlog

import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog
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

    override fun deleteAllSearchLog(): Completable {
        return searchLogLocalDataSource.deleteAllSearchLog()
            .subscribeOn(Schedulers.io())
    }

}