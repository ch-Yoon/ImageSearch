package com.ch.yoon.imagesearch.data.local.room

import com.ch.yoon.imagesearch.data.local.room.dao.ImageDAO
import com.ch.yoon.imagesearch.data.local.room.entity.mapper.ImageDocumentEntityMapper
import com.ch.yoon.imagesearch.data.repository.image.ImageLocalDataSource
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import io.reactivex.Completable

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
class ImageLocalDataSourceImpl(
    private val imageDAO: ImageDAO
) : ImageLocalDataSource {

    override fun saveFavoriteImageDocument(imageDocument: ImageDocument): Completable {
        val imageDocumentEntity = ImageDocumentEntityMapper.toEntity(imageDocument)
        return imageDAO.insertOrUpdateImageDocument(imageDocumentEntity)
    }

}