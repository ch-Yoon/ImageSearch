package com.ch.yoon.local.room.model.mapper

import com.ch.yoon.local.room.model.ImageDocumentEntity
import com.ch.yoon.data.model.image.ImageDocument

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object ImageDocumentEntityMapper {

    fun toEntity(imageDocument: com.ch.yoon.data.model.image.ImageDocument): ImageDocumentEntity {
        return imageDocument.run {
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

    fun fromEntityList(imageDocumentEntityList: List<ImageDocumentEntity>): List<com.ch.yoon.data.model.image.ImageDocument> {
        return imageDocumentEntityList.map { fromEntity(it) }
    }

    fun fromEntity(imageDocumentEntity: ImageDocumentEntity): com.ch.yoon.data.model.image.ImageDocument {
        return imageDocumentEntity.run {
            com.ch.yoon.data.model.image.ImageDocument(
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
}