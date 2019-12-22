package com.ch.yoon.local.room

import com.ch.yoon.local.room.dao.ImageDAO
import com.ch.yoon.local.room.model.mapper.ImageDocumentEntityMapper
import com.ch.yoon.local.room.transformer.error.CompletableExceptionTransformer
import com.ch.yoon.local.room.transformer.error.SingleExceptionTransformer
import com.ch.yoon.imagesearch.data.repository.image.ImageLocalDataSource
import com.ch.yoon.data.model.image.ImageDocument
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
class ImageLocalDataSourceImpl(
    private val imageDAO: ImageDAO
) : ImageLocalDataSource {

    override fun saveFavoriteImageDocument(imageDocument: com.ch.yoon.data.model.image.ImageDocument): Completable {
        val imageDocumentEntity = ImageDocumentEntityMapper.toEntity(imageDocument)
        return imageDAO.insertOrUpdateImageDocument(imageDocumentEntity)
            .compose(CompletableExceptionTransformer())
    }

    override fun deleteFavoriteImageDocument(imageDocument: com.ch.yoon.data.model.image.ImageDocument): Completable {
        return imageDAO.deleteImageDocument(imageDocument.id)
            .compose(CompletableExceptionTransformer())
    }

    override fun getAllFavoriteImages(): Single<List<com.ch.yoon.data.model.image.ImageDocument>> {
        return imageDAO.selectAllImageDocument()
            .map { ImageDocumentEntityMapper.fromEntityList(it) }
            .compose(SingleExceptionTransformer())
    }
}