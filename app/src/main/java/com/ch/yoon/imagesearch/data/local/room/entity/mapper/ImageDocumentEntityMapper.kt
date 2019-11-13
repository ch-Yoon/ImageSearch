package com.ch.yoon.imagesearch.data.local.room.entity.mapper

import com.ch.yoon.imagesearch.data.local.room.entity.ImageDocumentEntity
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object ImageDocumentEntityMapper {

    fun toEntity(imageDocument: ImageDocument): ImageDocumentEntity = imageDocument.run {
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

}