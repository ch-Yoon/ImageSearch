package com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao

import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error.ImageSearchError
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error.ImageSearchException
import io.reactivex.Single
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageSearchRemoteDataSourceImpl(
    private val imageSearchApi: ImageSearchApi
) : ImageSearchRemoteDataSource {

    override fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse> {
        return imageSearchRequest.run {
            imageSearchApi.searchImageList(keyword, imageSortType.type, pageNumber, requiredSize)
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