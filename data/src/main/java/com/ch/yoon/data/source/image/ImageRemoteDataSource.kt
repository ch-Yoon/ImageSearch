package com.ch.yoon.data.source.image

import com.ch.yoon.data.model.image.response.ImageSearchResponse
import com.ch.yoon.remote.kakao.model.request.ImageSearchRequest
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRemoteDataSource {

    fun getImages(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

}