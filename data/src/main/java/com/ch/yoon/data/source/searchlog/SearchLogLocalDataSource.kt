package com.ch.yoon.data.source.searchlog

import com.ch.yoon.data.model.searchlog.SearchLogEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface SearchLogLocalDataSource {

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLogEntity>

    fun getAllSearchLogs(): Single<List<SearchLogEntity>>

    fun deleteSearchLog(searchLogEntity: SearchLogEntity): Completable

    fun deleteAllSearchLogs(): Completable

}