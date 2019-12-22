package com.ch.yoon.domain.repository

import com.ch.yoon.domain.model.searchlog.SearchLog
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-11-12
 **/
interface SearchLogRepository {

    fun deleteSearchLog(searchLog: SearchLog): Completable

    fun getAllSearchLogs(): Single<List<SearchLog>>

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog>

    fun deleteAllSearchLogs(): Completable

}