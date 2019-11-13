package com.ch.yoon.imagesearch.data.repository.image

import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRepositoryImpl(
    private val imageLocalDataSource: ImageLocalDataSource,
    private val imageRemoteDataSource: ImageRemoteDataSource
) : ImageRepository {

    override fun requestImageList(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse> {
        return imageRemoteDataSource.requestImageList(imageSearchRequest)
            .subscribeOn(Schedulers.io())
    }

    override fun saveFavoriteImageDocument(imageDocument: ImageDocument): Completable {
        return imageLocalDataSource.saveFavoriteImageDocument(imageDocument)
            .subscribeOn(Schedulers.io())
    }
}