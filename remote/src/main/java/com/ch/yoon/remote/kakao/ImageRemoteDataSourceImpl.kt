package com.ch.yoon.remote.kakao

import com.ch.yoon.remote.kakao.model.request.ImageSearchRequest
import com.ch.yoon.data.model.image.response.ImageSearchResponseEntity
import com.ch.yoon.data.source.image.ImageRemoteDataSource
import com.ch.yoon.remote.kakao.model.response.mapper.KakaoImageSearchResponseMapper
import com.ch.yoon.remote.kakao.model.transformer.error.SingleExceptionTransformer
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRemoteDataSourceImpl(
    private val kakaoSearchApi: KakaoSearchApi
) : ImageRemoteDataSource {

    override fun getImages(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponseEntity> {
        return imageSearchRequest.run {
            kakaoSearchApi.searchImageList(keyword, imageSortType.type, pageNumber, requiredSize)
                .map { KakaoImageSearchResponseMapper.fromEntity(it) }
                .compose(SingleExceptionTransformer())
        }
    }
}