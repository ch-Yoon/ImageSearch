package com.ch.yoon.imagesearch.data.repository.searchlog

import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLogModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-11-12
 **/
interface SearchLogRepository {

    fun deleteSearchLog(searchLogModel: SearchLogModel): Completable

    fun requestSearchLogList(): Single<List<SearchLogModel>>

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLogModel>

    fun deleteAllSearchLog(): Completable

}