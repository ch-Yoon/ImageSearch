package com.ch.yoon.imagesearch.data.repository.image

import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLogModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRepository {

    fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

    fun saveFavoriteImageDocument(imageDocument: ImageDocument): Completable

}