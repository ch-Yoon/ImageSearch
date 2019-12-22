package com.ch.yoon.data.source.image

import com.ch.yoon.data.model.image.request.ImageSearchRequestEntity
import com.ch.yoon.data.model.image.response.ImageSearchResponseEntity
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
interface ImageRemoteDataSource {

    fun getImages(imageSearchRequestEntity: ImageSearchRequestEntity): Single<ImageSearchResponseEntity>

}