package com.ch.yoon.data.source.image

import com.ch.yoon.data.model.image.response.ImageDocument
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
interface ImageLocalDataSource {

    fun saveFavoriteImageDocument(imageDocument: ImageDocument): Completable

    fun deleteFavoriteImageDocument(imageDocument: ImageDocument): Completable

    fun getAllFavoriteImages(): Single<List<ImageDocument>>

}