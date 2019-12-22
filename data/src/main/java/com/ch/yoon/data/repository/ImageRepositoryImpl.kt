package com.ch.yoon.data.repository

import com.ch.yoon.data.model.image.response.ImageDocumentEntity
import com.ch.yoon.data.model.image.response.ImageSearchResponseEntity
import com.ch.yoon.data.source.image.ImageLocalDataSource
import com.ch.yoon.data.source.image.ImageRemoteDataSource
import com.ch.yoon.domain.model.image.request.ImageSearchRequest
import com.ch.yoon.domain.repository.ImageRepository
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

    private val favoriteChangePublishSubject = PublishSubject.create<ImageDocumentEntity>()

    override fun getImages(imageSearchRequest: ImageSearchRequest): Single<ImageSearchResponseEntity> {
        return Single.zip(
            imageRemoteDataSource.getImages(imageSearchRequest)
                .subscribeOn(Schedulers.io()),
            imageLocalDataSource.getAllFavoriteImages()
                .map { favoriteList -> favoriteList.associateBy({ it.id }, {it}) }
                .subscribeOn(Schedulers.io()),
            BiFunction { responseEntity: ImageSearchResponseEntity, favoriteMap: Map<String, ImageDocumentEntity> ->
                val newList = responseEntity.imageDocumentEntities.map { favoriteMap[it.id] ?: it }
                ImageSearchResponseEntity(responseEntity.imageSearchMetaEntity, newList)
            }
        ).subscribeOn(Schedulers.io())
    }

    override fun getAllFavoriteImages(): Single<List<ImageDocumentEntity>> {
        return imageLocalDataSource.getAllFavoriteImages()
            .subscribeOn(Schedulers.io())
    }

    override fun saveFavoriteImage(imageDocumentEntity: ImageDocumentEntity): Completable {
        return imageLocalDataSource.saveFavoriteImageDocument(imageDocumentEntity)
            .doOnComplete { favoriteChangePublishSubject.onNext(imageDocumentEntity) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteFavoriteImage(imageDocumentEntity: ImageDocumentEntity): Completable {
        return imageLocalDataSource.deleteFavoriteImageDocument(imageDocumentEntity)
            .doOnComplete { favoriteChangePublishSubject.onNext(imageDocumentEntity) }
            .subscribeOn(Schedulers.io())
    }

    override fun observeChangingFavoriteImage(): Observable<ImageDocumentEntity> {
        return favoriteChangePublishSubject.subscribeOn(Schedulers.io())
    }
}