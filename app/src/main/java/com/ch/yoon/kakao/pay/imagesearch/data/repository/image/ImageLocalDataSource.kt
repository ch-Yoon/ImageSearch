package com.ch.yoon.kakao.pay.imagesearch.data.repository.image

import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.SearchLog
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageLocalDataSource {

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog>

    fun selectAllSearchLog(): Single<List<SearchLog>>

    fun deleteSearchLog(searchLog: SearchLog): Completable

}