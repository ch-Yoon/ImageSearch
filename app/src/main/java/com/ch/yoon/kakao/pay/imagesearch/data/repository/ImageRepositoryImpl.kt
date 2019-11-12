package com.ch.yoon.kakao.pay.imagesearch.data.repository

import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageSearchResponse
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLogModel
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

    override fun requestSearchLogList(): Single<List<SearchLogModel>> {
        return imageLocalDataSource.selectAllSearchLog()
            .subscribeOn(Schedulers.io())
    }

    override fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse> {
        return imageRemoteDataSource.requestImageList(imageSearchRequest)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSearchLog(searchLogModel: SearchLogModel): Completable {
        return imageLocalDataSource.deleteSearchLog(searchLogModel)
            .subscribeOn(Schedulers.io())
    }

    override fun insertOrUpdateSearchLog(keyword: String): Single<SearchLogModel> {
        return imageLocalDataSource.insertOrUpdateSearchLog(keyword)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteAllSearchLog(): Completable {
        return imageLocalDataSource.deleteAllSearchLog()
            .subscribeOn(Schedulers.io())
    }
}