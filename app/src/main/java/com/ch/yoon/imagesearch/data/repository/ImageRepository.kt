package com.ch.yoon.imagesearch.data.repository

import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.model.ImageSearchResponse
import com.ch.yoon.imagesearch.data.repository.model.SearchLogModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRepository {

    fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

    fun deleteSearchLog(searchLogModel: SearchLogModel): Completable

    fun requestSearchLogList(): Single<List<SearchLogModel>>

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLogModel>

    fun deleteAllSearchLog(): Completable

}