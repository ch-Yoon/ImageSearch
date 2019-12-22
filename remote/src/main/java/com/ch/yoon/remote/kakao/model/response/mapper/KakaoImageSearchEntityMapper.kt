package com.ch.yoon.remote.kakao.model.response.mapper

import com.ch.yoon.remote.kakao.model.response.KakaoImageSearchResponse
import com.ch.yoon.data.model.image.ImageDocument
import com.ch.yoon.data.model.image.ImageSearchMeta
import com.ch.yoon.data.model.image.ImageSearchResponse

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object KakaoImageSearchEntityMapper {

    fun fromEntity(remoteEntity: KakaoImageSearchResponse): com.ch.yoon.data.model.image.ImageSearchResponse {
        return remoteEntity.run {
            val imageSearchMeta = kakaoImageSearchMeta.run {
                com.ch.yoon.data.model.image.ImageSearchMeta(isEnd)
            }

            val imageDocumentList = kakaoImageDocuments.map {
                com.ch.yoon.data.model.image.ImageDocument(
                    "${it.imageUrl}&${it.docUrl}",
                    it.collection,
                    it.thumbnailUrl,
                    it.imageUrl,
                    it.width,
                    it.height,
                    it.displaySiteName,
                    it.docUrl,
                    it.dateTime,
                    false
                )
            }

            com.ch.yoon.data.model.image.ImageSearchResponse(imageSearchMeta, imageDocumentList)
        }
    }

}