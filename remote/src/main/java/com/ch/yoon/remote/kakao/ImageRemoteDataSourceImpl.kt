package com.ch.yoon.remote.kakao

import com.ch.yoon.data.model.image.request.ImageSearchRequestEntity
import com.ch.yoon.data.model.image.response.ImageSearchResponseEntity
import com.ch.yoon.data.source.image.ImageRemoteDataSource
import com.ch.yoon.remote.kakao.model.image.response.KakaoImageSearchResponse
import com.ch.yoon.remote.kakao.model.image.mapper.EntityMapper
import com.ch.yoon.remote.kakao.model.transformer.error.SingleExceptionTransformer
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRemoteDataSourceImpl(
    private val kakaoSearchApi: KakaoSearchApi,
    private val imageSearchResponseEntity: EntityMapper<KakaoImageSearchResponse, ImageSearchResponseEntity>
) : ImageRemoteDataSource {

    override fun getImages(imageSearchRequestEntity: ImageSearchRequestEntity): Single<ImageSearchResponseEntity> {
        return imageSearchRequestEntity.run {
            kakaoSearchApi.searchImageList(keyword, imageSortTypeEntity.type, pageNumber, requiredSize)
                .map { imageSearchResponseEntity.toEntity(it) }
                .compose(SingleExceptionTransformer())
        }
    }
}