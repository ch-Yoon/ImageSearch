package com.ch.yoon.imagesearch.data.repository.searchlog

import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLogModel
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

    override fun requestSearchLogList(): Single<List<SearchLogModel>> {
        return searchLogLocalDataSource.selectAllSearchLog()
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSearchLog(searchLogModel: SearchLogModel): Completable {
        return searchLogLocalDataSource.deleteSearchLog(searchLogModel)
            .subscribeOn(Schedulers.io())
    }

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLogModel> {
        return searchLogLocalDataSource.insertOrUpdateSearchLog(keyword)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteAllSearchLog(): Completable {
        return searchLogLocalDataSource.deleteAllSearchLog()
            .subscribeOn(Schedulers.io())
    }

}