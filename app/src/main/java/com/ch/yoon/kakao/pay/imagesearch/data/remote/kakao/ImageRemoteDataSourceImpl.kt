package com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao

import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.response.ImageSearchResponse
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.response.error.ImageSearchError
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.response.error.ImageSearchException
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRemoteDataSource
import io.reactivex.Single
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRemoteDataSourceImpl(
    private val searchApi: SearchApi
) : ImageRemoteDataSource {

    override fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse> {
        return imageSearchRequest.run {
            searchApi.searchImageList(keyword, imageSortType.type, pageNumber, requiredSize)
                .onErrorResumeNext { throwable ->
                    val errorMessage: String
                    val imageSearchError: ImageSearchError

                    when (throwable) {
                        is HttpException -> {
                            errorMessage = throwable.message()
                            imageSearchError = ImageSearchError.convertToImageSearchError(throwable.code())
                        }
                        is UnknownHostException -> {
                            errorMessage = throwable.message ?: "network not connecting error"
                            imageSearchError = ImageSearchError.NETWORK_NOT_CONNECTING_ERROR
                        }
                        else -> {
                            errorMessage = throwable.message ?: "unknown error"
                            imageSearchError = ImageSearchError.UNKNOWN_ERROR
                        }
                    }

                    Single.error(ImageSearchException(errorMessage, imageSearchError))
                }
        }
    }

}