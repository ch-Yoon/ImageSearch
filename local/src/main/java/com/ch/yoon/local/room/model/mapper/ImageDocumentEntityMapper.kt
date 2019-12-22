package com.ch.yoon.local.room.model.mapper

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object ImageDocumentEntityMapper {

    fun toEntity(imageDocumentEntity: com.ch.yoon.data.model.image.response.ImageDocumentEntity): ImageDocumentEntity {
        return imageDocumentEntity.run {
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

    fun fromEntityList(imageDocumentEntityList: List<ImageDocumentEntity>): List<com.ch.yoon.data.model.image.response.ImageDocumentEntity> {
        return imageDocumentEntityList.map { fromEntity(it) }
    }

    fun fromEntity(imageDocumentEntity: ImageDocumentEntity): com.ch.yoon.data.model.image.response.ImageDocumentEntity {
        return imageDocumentEntity.run {
            com.ch.yoon.data.model.image.response.ImageDocumentEntity(
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