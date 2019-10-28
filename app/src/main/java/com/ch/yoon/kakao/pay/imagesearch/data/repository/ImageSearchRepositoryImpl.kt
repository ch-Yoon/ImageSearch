package com.ch.yoon.kakao.pay.imagesearch.data.repository

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageSearchLocalDataSource
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResult
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.ImageSearchRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageSearchRepositoryImpl(
    private val imageSearchRemoteDataSource: ImageSearchRemoteDataSource,
    private val imageSearchLocalDataSource: ImageSearchLocalDataSource
) : ImageSearchRepository {

    override fun requestSearchLogList(): Single<List<SearchLog>> {
        return imageSearchLocalDataSource.selectAllSearchLog()
            .subscribeOn(Schedulers.io())
    }

    override fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResult> {
        return imageSearchRemoteDataSource.requestImageList(imageSearchRequest)
            .map { imageSearchResponse ->
                imageSearchResponse.run {
                    ImageSearchResult(imageSearchRequest, searchMetaInfo, imageDocumentList)
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSearchLog(keyword: String): Completable {
        return imageSearchLocalDataSource.deleteSearchLog(keyword)
            .subscribeOn(Schedulers.io())
    }

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog> {
        return imageSearchLocalDataSource.insertOrUpdateSearchLog(keyword)
            .subscribeOn(Schedulers.io())
    }
}