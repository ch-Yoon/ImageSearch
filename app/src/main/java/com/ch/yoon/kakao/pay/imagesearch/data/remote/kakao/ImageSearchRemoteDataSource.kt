package com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao

import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageSearchRemoteDataSource {

    fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

}