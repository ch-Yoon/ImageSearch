package com.ch.yoon.kakao.pay.imagesearch.data.repository.image

import com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.ImageSearchResponse
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.SearchLog
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRepository {

    fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

    fun deleteSearchLog(searchLog: SearchLog): Completable

    fun requestSearchLogList(): Single<List<SearchLog>>

    fun insertOrUpdateSearchLog(keyword: String): Single<SearchLog>

}