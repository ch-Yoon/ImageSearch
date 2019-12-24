package com.ch.yoon.remote

import com.ch.yoon.data.model.image.request.ImageSearchRequestEntity
import com.ch.yoon.data.model.image.response.ImageSearchResponseEntity
import com.ch.yoon.data.source.image.ImageRemoteDataSource
import com.ch.yoon.remote.extension.composeDataLayerException
import com.ch.yoon.remote.model.image.KakaoImageSearchResponse
import com.ch.yoon.remote.mapper.EntityMapper
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRemoteDataSourceImpl(
    private val kakaoSearchApi: KakaoSearchApi,
    private val imageSearchResponseEntityMapper: EntityMapper<KakaoImageSearchResponse, ImageSearchResponseEntity>
) : ImageRemoteDataSource {

    override fun getImages(imageSearchRequestEntity: ImageSearchRequestEntity): Single<ImageSearchResponseEntity> {
        return imageSearchRequestEntity.run {
            kakaoSearchApi.searchImageList(keyword, imageSortTypeEntity.type, pageNumber, requiredSize)
                .map { imageSearchResponseEntityMapper.fromRemote(it) }
                .composeDataLayerException()
        }
    }

}