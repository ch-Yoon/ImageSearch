package com.ch.yoon.data.source.image

import com.ch.yoon.data.model.image.response.ImageDocumentEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
interface ImageLocalDataSource {

    fun saveFavoriteImageDocument(imageDocumentEntity: ImageDocumentEntity): Completable

    fun deleteFavoriteImageDocument(imageDocumentEntity: ImageDocumentEntity): Completable

    fun getAllFavoriteImages(): Single<List<ImageDocumentEntity>>

}