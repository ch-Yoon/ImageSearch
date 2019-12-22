package com.ch.yoon.remote.kakao

import com.ch.yoon.remote.kakao.model.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.image.ImageRemoteDataSource
import com.ch.yoon.data.model.image.ImageSearchResponse
import com.ch.yoon.remote.kakao.model.response.mapper.KakaoImageSearchEntityMapper
import com.ch.yoon.remote.kakao.model.transformer.error.SingleExceptionTransformer
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRemoteDataSourceImpl(
    private val kakaoSearchApi: KakaoSearchApi
) : ImageRemoteDataSource {

    override fun getImages(imageSearchRequest: ImageSearchRequest): Single<com.ch.yoon.data.model.image.ImageSearchResponse> {
        return imageSearchRequest.run {
            kakaoSearchApi.searchImageList(keyword, imageSortType.type, pageNumber, requiredSize)
                .map { KakaoImageSearchEntityMapper.fromEntity(it) }
                .compose(SingleExceptionTransformer())
        }
    }
}