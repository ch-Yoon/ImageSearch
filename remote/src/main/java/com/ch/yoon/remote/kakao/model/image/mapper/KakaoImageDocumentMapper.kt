package com.ch.yoon.remote.kakao.model.image.mapper

import com.ch.yoon.data.model.image.response.ImageDocumentEntity
import com.ch.yoon.remote.kakao.model.image.response.KakaoImageDocument

/**
 * Creator : ch-yoon
 * Date : 2019-12-23.
 */
object KakaoImageDocumentMapper : EntityMapper<KakaoImageDocument, ImageDocumentEntity> {

    override fun toEntity(remoteModel: KakaoImageDocument): ImageDocumentEntity {
        return remoteModel.run {
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