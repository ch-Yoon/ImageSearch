package com.ch.yoon.imagesearch.data.local.room

import com.ch.yoon.imagesearch.data.local.room.dao.SearchLogDAO
import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntity
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogLocalDataSource
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLogModel
import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntityMapper
import com.ch.yoon.imagesearch.data.local.room.error.CompletableExceptionTransformer
import com.ch.yoon.imagesearch.data.local.room.error.SingleExceptionTransformer
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class SearchLogLocalDataSourceImpl(
    private val searchLogDAO: SearchLogDAO
) : SearchLogLocalDataSource {

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLogModel> {
        val newSearchLogEntity = SearchLogEntity(keyword, System.currentTimeMillis())
        return searchLogDAO.insertOrUpdateSearchLog(newSearchLogEntity)
            .toSingle { newSearchLogEntity }
            .map { searchLogModel -> SearchLogEntityMapper.toEntity(searchLogModel) }
            .compose(SingleExceptionTransformer())
    }

    override fun selectAllSearchLog(): Single<List<SearchLogModel>> {
        return searchLogDAO.selectAllSearchLog()
            .map { searchLogModeList -> SearchLogEntityMapper.toEntityList(searchLogModeList) }
            .compose(SingleExceptionTransformer())
    }

    override fun deleteSearchLog(searchLogModel: SearchLogModel): Completable {
        return searchLogDAO.deleteSearchLog(searchLogModel.keyword, searchLogModel.time)
            .compose(CompletableExceptionTransformer())
    }

    override fun deleteAllSearchLog(): Completable {
        return searchLogDAO.deleteAllSearchLog()
            .compose(CompletableExceptionTransformer())
    }
}