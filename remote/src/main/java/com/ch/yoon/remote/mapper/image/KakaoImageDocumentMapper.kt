package com.ch.yoon.remote.mapper.image

import com.ch.yoon.data.model.image.response.ImageDocumentEntity
import com.ch.yoon.remote.mapper.EntityMapper
import com.ch.yoon.remote.model.image.KakaoImageDocument

/**
 * Creator : ch-yoon
 * Date : 2019-12-23.
 */
object KakaoImageDocumentMapper : EntityMapper<KakaoImageDocument, ImageDocumentEntity> {

    override fun fromRemote(model: KakaoImageDocument): ImageDocumentEntity {
        return model.run {
            ImageDocumentEntity(
                "${imageUrl}&${docUrl}",
                collection,
                thumbnailUrl,
                imageUrl,
                width,
                height,
                displaySiteName,
                docUrl,
                dateTime,
                false
            )
        }
    }
}