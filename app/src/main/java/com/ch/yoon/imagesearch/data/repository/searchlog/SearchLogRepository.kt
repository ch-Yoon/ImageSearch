package com.ch.yoon.imagesearch.data.repository.searchlog

import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog
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

    fun deleteAllSearchLog(): Completable

}