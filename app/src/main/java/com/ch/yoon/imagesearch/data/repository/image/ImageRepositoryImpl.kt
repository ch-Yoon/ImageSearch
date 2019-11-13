package com.ch.yoon.imagesearch.data.repository.image

import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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
        return Single.zip(
            imageRemoteDataSource.requestImageList(imageSearchRequest).subscribeOn(Schedulers.io()),
            imageLocalDataSource.selectAllFavoriteImageDocuments().subscribeOn(Schedulers.io()),
            BiFunction<ImageSearchResponse, List<ImageDocument>, ImageSearchResponse> { response, favoriteList ->
                val map = favoriteList.associateBy({ it.id }, { it })
                val newList = response.imageDocumentList.map { document ->
                    map[document.id]?.let { it } ?: document
                }
                ImageSearchResponse(response.imageSearchMeta, newList)
            }
        ).subscribeOn(Schedulers.io())
    }

    override fun saveFavoriteImageDocument(imageDocument: ImageDocument): Completable {
        return imageLocalDataSource.saveFavoriteImageDocument(imageDocument)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteFavoriteImageDocument(id: String): Completable {
        return imageLocalDataSource.deleteFavoriteImageDocument(id)
            .subscribeOn(Schedulers.io())
    }
}