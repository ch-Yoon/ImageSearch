package com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao

import com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.ImageRemoteDataSource
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.ImageSearchResponse
import com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.response.KakaoImageSearchEntityMapper
import com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.error.SingleExceptionTransformer
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRemoteDataSourceImpl(
    private val kakaoSearchService: KakaoSearchService
) : ImageRemoteDataSource {

    override fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse> {
        return imageSearchRequest.run {
            kakaoSearchService.searchImageList(keyword, imageSortType.type, pageNumber, requiredSize)
                .map { remoteImageSearchResponse ->
                    KakaoImageSearchEntityMapper.toEntity(remoteImageSearchResponse)
                }
                .compose(SingleExceptionTransformer())
        }
    }

}