package com.ch.yoon.local.room

import com.ch.yoon.local.room.dao.ImageDAO
import com.ch.yoon.local.room.model.mapper.ImageDocumentEntityMapper
import com.ch.yoon.local.room.transformer.error.CompletableExceptionTransformer
import com.ch.yoon.local.room.transformer.error.SingleExceptionTransformer
import com.ch.yoon.data.model.image.response.ImageDocumentEntity
import com.ch.yoon.data.source.image.ImageLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
class ImageLocalDataSourceImpl(
    private val imageDAO: ImageDAO
) : ImageLocalDataSource {

    override fun saveFavoriteImageDocument(imageDocumentEntity: ImageDocumentEntity): Completable {
        val imageDocumentEntity = ImageDocumentEntityMapper.toEntity(imageDocumentEntity)
        return imageDAO.insertOrUpdateImageDocument(imageDocumentEntity)
            .compose(CompletableExceptionTransformer())
    }

    override fun deleteFavoriteImageDocument(imageDocumentEntity: ImageDocumentEntity): Completable {
        return imageDAO.deleteImageDocument(imageDocumentEntity.id)
            .compose(CompletableExceptionTransformer())
    }

    override fun getAllFavoriteImages(): Single<List<ImageDocumentEntity>> {
        return imageDAO.selectAllImageDocument()
            .map { ImageDocumentEntityMapper.fromEntityList(it) }
            .compose(SingleExceptionTransformer())
    }
}