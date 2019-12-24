package com.ch.yoon.local

import com.ch.yoon.local.dao.ImageDAO
import com.ch.yoon.local.mapper.image.ImageDocumentEntityMapper
import com.ch.yoon.data.model.image.response.ImageDocumentEntity
import com.ch.yoon.data.source.image.ImageLocalDataSource
import com.ch.yoon.local.extension.composeDataLayerException
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
        val localImageDocument = ImageDocumentEntityMapper.toLocal(imageDocumentEntity)
        return imageDAO.insertOrUpdateImageDocument(localImageDocument)
            .composeDataLayerException()
    }

    override fun deleteFavoriteImageDocument(imageDocumentEntity: ImageDocumentEntity): Completable {
        return imageDAO.deleteImageDocument(imageDocumentEntity.id)
            .composeDataLayerException()
    }

    override fun getAllFavoriteImages(): Single<List<ImageDocumentEntity>> {
        return imageDAO.selectAllImageDocument()
            .map { ImageDocumentEntityMapper.fromLocal(it) }
            .composeDataLayerException()
    }
}