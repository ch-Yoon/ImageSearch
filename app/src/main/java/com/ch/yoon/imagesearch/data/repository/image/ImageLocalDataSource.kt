package com.ch.yoon.imagesearch.data.repository.image

import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import io.reactivex.Completable

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
interface ImageLocalDataSource {

    fun saveFavoriteImageDocument(imageDocument: ImageDocument): Completable

}