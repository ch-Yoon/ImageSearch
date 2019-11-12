package com.ch.yoon.imagesearch.data.repository

import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.model.ImageSearchResponse
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRemoteDataSource {

    fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse>

}