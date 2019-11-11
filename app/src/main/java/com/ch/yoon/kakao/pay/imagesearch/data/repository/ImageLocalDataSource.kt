package com.ch.yoon.kakao.pay.imagesearch.data.repository

import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLogModel
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

}