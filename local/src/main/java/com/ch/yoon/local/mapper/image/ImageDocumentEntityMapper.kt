package com.ch.yoon.local.mapper.image

import com.ch.yoon.data.model.image.response.ImageDocumentEntity
import com.ch.yoon.local.mapper.EntityMapper
import com.ch.yoon.local.model.image.LocalImageDocument

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object ImageDocumentEntityMapper : EntityMapper<LocalImageDocument, ImageDocumentEntity> {

    override fun fromLocal(model: LocalImageDocument): ImageDocumentEntity {
        return model.run {
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
                isFavorite
            )
        }
    }

    override fun fromLocal(models: List<LocalImageDocument>): List<ImageDocumentEntity> {
        return models.map { fromLocal(it) }
    }

    override fun toLocal(model: ImageDocumentEntity): LocalImageDocument {
        return model.run {
            LocalImageDocument(
                id,
                collection,
                thumbnailUrl,
                imageUrl,
                width,
                height,
                displaySiteName,
                docUrl,
                dateTime,
                isFavorite
            )
        }
    }

    override fun toLocal(models: List<ImageDocumentEntity>): List<LocalImageDocument> {
        return models.map { toLocal(it) }
    }

}