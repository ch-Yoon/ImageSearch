package com.ch.yoon.imagesearch.data.repository.image

import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
class ImageRepositoryImpl(
    private val imageLocalDataSource: ImageLocalDataSource,
    private val imageRemoteDataSource: ImageRemoteDataSource
) : ImageRepository {

    private val favoriteChangePublishSubject = PublishSubject.create<ImageDocument>()

    override fun getImages(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponse> {
        return Single.zip(
            imageRemoteDataSource.getImages(imageSearchRequest)
                .subscribeOn(Schedulers.io()),
            imageLocalDataSource.getAllFavoriteImages()
                .map { favoriteList -> favoriteList.associateBy({ it.id }, {it}) }
                .subscribeOn(Schedulers.io()),
            BiFunction { response: ImageSearchResponse, favoriteMap: Map<String, ImageDocument> ->
                val newList = response.imageDocumentList.map { favoriteMap[it.id] ?: it }
                ImageSearchResponse(response.imageSearchMeta, newList)
            }
        ).subscribeOn(Schedulers.io())
    }

    override fun getAllFavoriteImages(): Single<List<ImageDocument>> {
        return imageLocalDataSource.getAllFavoriteImages()
            .subscribeOn(Schedulers.io())
    }

    override fun saveFavoriteImage(imageDocument: ImageDocument): Completable {
        return imageLocalDataSource.saveFavoriteImageDocument(imageDocument)
            .doOnComplete { favoriteChangePublishSubject.onNext(imageDocument) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteFavoriteImage(imageDocument: ImageDocument): Completable {
        return imageLocalDataSource.deleteFavoriteImageDocument(imageDocument)
            .doOnComplete { favoriteChangePublishSubject.onNext(imageDocument) }
            .subscribeOn(Schedulers.io())
    }

    override fun observeChangingFavoriteImage(): Observable<ImageDocument> {
        return favoriteChangePublishSubject.subscribeOn(Schedulers.io())
    }
}