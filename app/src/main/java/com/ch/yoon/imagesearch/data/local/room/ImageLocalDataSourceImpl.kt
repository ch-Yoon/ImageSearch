package com.ch.yoon.imagesearch.data.local.room

import com.ch.yoon.imagesearch.data.local.room.dao.ImageDAO
import com.ch.yoon.imagesearch.data.local.room.entity.ImageDocumentEntity
import com.ch.yoon.imagesearch.data.repository.image.ImageLocalDataSource
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
class ImageLocalDataSourceImpl(
    private val imageDAO: ImageDAO
) : ImageLocalDataSource {

    override fun saveFavoriteImageDocument(imageDocument: ImageDocument): Completable {
        val value = imageDocument.run {
            ImageDocumentEntity(
                id,
                collection,
                thumbnailUrl,
                imageUrl,
                width,
                height,
                displaySiteName,
                docUrl,
                dateTime,
                isFavorite)
        }

        return imageDAO.insertOrUpdateImageDocument(value)
    }

}