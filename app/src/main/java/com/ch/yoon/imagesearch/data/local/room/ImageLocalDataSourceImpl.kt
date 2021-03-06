package com.ch.yoon.imagesearch.data.local.room

import com.ch.yoon.imagesearch.data.local.room.dao.ImageDAO
import com.ch.yoon.imagesearch.data.local.room.entity.mapper.ImageDocumentEntityMapper
import com.ch.yoon.imagesearch.data.local.room.transformer.error.CompletableExceptionTransformer
import com.ch.yoon.imagesearch.data.local.room.transformer.error.SingleExceptionTransformer
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
        val imageDocumentEntity = ImageDocumentEntityMapper.toEntity(imageDocument)
        return imageDAO.insertOrUpdateImageDocument(imageDocumentEntity)
            .compose(CompletableExceptionTransformer())
    }

    override fun deleteFavoriteImageDocument(imageDocument: ImageDocument): Completable {
        return imageDAO.deleteImageDocument(imageDocument.id)
            .compose(CompletableExceptionTransformer())
    }

    override fun getAllFavoriteImages(): Single<List<ImageDocument>> {
        return imageDAO.selectAllImageDocument()
            .map { ImageDocumentEntityMapper.fromEntityList(it) }
            .compose(SingleExceptionTransformer())
    }
}