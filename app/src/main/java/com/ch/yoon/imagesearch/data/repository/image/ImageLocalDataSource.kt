package com.ch.yoon.imagesearch.data.repository.image

import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
interface ImageLocalDataSource {

    fun saveFavoriteImageDocument(imageDocument: ImageDocument): Completable

    fun deleteFavoriteImageDocument(imageDocument: ImageDocument): Completable

    fun selectAllFavoriteImageDocumentList(): Single<List<ImageDocument>>

}