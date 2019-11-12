package com.ch.yoon.imagesearch.data.repository

import com.ch.yoon.imagesearch.data.repository.model.SearchLogModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageLocalDataSource {

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLogModel>

    fun selectAllSearchLog(): Single<List<SearchLogModel>>

    fun deleteSearchLog(searchLogModel: SearchLogModel): Completable

    fun deleteAllSearchLog(): Completable

}