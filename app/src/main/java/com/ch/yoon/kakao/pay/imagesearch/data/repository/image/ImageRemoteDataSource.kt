package com.ch.yoon.kakao.pay.imagesearch.data.repository.image

import com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.ImageSearchResponse
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRemoteDataSource {

    fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

}