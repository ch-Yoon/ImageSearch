package com.ch.yoon.data.source.searchlog

import com.ch.yoon.data.model.searchlog.SearchLog
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface SearchLogLocalDataSource {

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog>

    fun getAllSearchLogs(): Single<List<SearchLog>>

    fun deleteSearchLog(searchLog: SearchLog): Completable

    fun deleteAllSearchLogs(): Completable

}