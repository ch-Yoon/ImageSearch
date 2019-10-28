package com.ch.yoon.kakao.pay.imagesearch.data.local.room

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.dao.SearchLogDAO
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageSearchLocalDataSourceImpl(
    private val searchLogDAO: SearchLogDAO
) : ImageSearchLocalDataSource {

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog> {
        val newSearchLog = SearchLog(keyword, System.currentTimeMillis())
        return searchLogDAO.insertOrUpdateSearchLog(newSearchLog)
            .toSingle { newSearchLog }
    }

    override fun selectAllSearchLog(): Single<List<SearchLog>> {
        return searchLogDAO.selectAllSearchLog()
    }

    override fun deleteSearchLog(keyword: String): Completable {
        return searchLogDAO.deleteSearchLog(keyword)
    }

}