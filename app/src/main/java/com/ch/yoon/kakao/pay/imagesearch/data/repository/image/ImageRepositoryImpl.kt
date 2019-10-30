package com.ch.yoon.kakao.pay.imagesearch.data.repository.image

import com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.ImageSearchResponse
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.SearchLog
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRepositoryImpl(
    private val imageRemoteDataSource: ImageRemoteDataSource,
    private val imageLocalDataSource: ImageLocalDataSource
) : ImageRepository {

    override fun requestSearchLogList(): Single<List<SearchLog>> {
        return imageLocalDataSource.selectAllSearchLog()
            .subscribeOn(Schedulers.io())
    }

    override fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse> {
        return imageRemoteDataSource.requestImageList(imageSearchRequest)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSearchLog(searchLog: SearchLog): Completable {
        return imageLocalDataSource.deleteSearchLog(searchLog)
            .subscribeOn(Schedulers.io())
    }

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog> {
        return imageLocalDataSource.insertOrUpdateSearchLog(keyword)
            .subscribeOn(Schedulers.io())
    }
}