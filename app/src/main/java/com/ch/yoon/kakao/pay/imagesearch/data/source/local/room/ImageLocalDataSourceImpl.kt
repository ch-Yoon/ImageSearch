package com.ch.yoon.kakao.pay.imagesearch.data.source.local.room

import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.dao.SearchLogDAO
import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.entity.SearchLogModel
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.ImageLocalDataSource
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.entity.SearchLogEntityMapper
import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.error.CompletableExceptionTransformer
import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.error.SingleExceptionTransformer
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageLocalDataSourceImpl(
    private val searchLogDAO: SearchLogDAO
) : ImageLocalDataSource {

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog> {
        val newSearchLogModel = SearchLogModel(keyword, System.currentTimeMillis())
        return searchLogDAO.insertOrUpdateSearchLog(newSearchLogModel)
            .toSingle { newSearchLogModel }
            .map { searchLogModel -> SearchLogEntityMapper.toEntity(searchLogModel) }
            .compose(SingleExceptionTransformer())
    }

    override fun selectAllSearchLog(): Single<List<SearchLog>> {
        return searchLogDAO.selectAllSearchLog()
            .map { searchLogModeList -> SearchLogEntityMapper.toEntityList(searchLogModeList) }
            .compose(SingleExceptionTransformer())
    }

    override fun deleteSearchLog(searchLog: SearchLog): Completable {
        return searchLogDAO.deleteSearchLog(searchLog.keyword, searchLog.time)
            .compose(CompletableExceptionTransformer())
    }

}