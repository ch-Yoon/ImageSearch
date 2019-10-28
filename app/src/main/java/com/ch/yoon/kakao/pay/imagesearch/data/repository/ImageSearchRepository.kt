package com.ch.yoon.kakao.pay.imagesearch.data.repository

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResult
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageSearchRepository {

    fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResult>

    fun deleteSearchLog(keyword: String): Completable

    fun requestSearchLogList(): Single<List<SearchLog>>

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog>

}