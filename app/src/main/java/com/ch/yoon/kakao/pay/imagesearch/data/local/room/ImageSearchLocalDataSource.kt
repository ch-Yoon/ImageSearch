package com.ch.yoon.kakao.pay.imagesearch.data.local.room

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageSearchLocalDataSource {

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog>

    fun selectAllSearchLog(): Single<List<SearchLog>>

    fun deleteSearchLog(keyword: String): Completable

}