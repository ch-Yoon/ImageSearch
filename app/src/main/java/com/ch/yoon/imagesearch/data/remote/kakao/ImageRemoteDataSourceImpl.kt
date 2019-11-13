package com.ch.yoon.imagesearch.data.remote.kakao

import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.image.ImageRemoteDataSource
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import com.ch.yoon.imagesearch.data.remote.kakao.response.mapper.KakaoImageSearchEntityMapper
import com.ch.yoon.imagesearch.data.remote.kakao.error.SingleExceptionTransformer
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRemoteDataSourceImpl(
    private val kakaoSearchApi: KakaoSearchApi
) : ImageRemoteDataSource {

    override fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse> {
        return imageSearchRequest.run {
            kakaoSearchApi.searchImageList(keyword, imageSortType.type, pageNumber, requiredSize)
                .map { remoteImageSearchResponse ->
                    KakaoImageSearchEntityMapper.fromEntity(remoteImageSearchResponse)
                }
                .compose(SingleExceptionTransformer())
        }
    }

}