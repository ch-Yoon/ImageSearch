package com.ch.yoon.remote.kakao.model.response.mapper

import com.ch.yoon.data.model.image.response.ImageDocument
import com.ch.yoon.data.model.image.response.ImageSearchMeta
import com.ch.yoon.data.model.image.response.ImageSearchResponse
import com.ch.yoon.remote.kakao.model.response.KakaoImageSearchResponse

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object KakaoImageSearchEntityMapper {

    fun fromEntity(remoteEntity: KakaoImageSearchResponse): ImageSearchResponse {
        return remoteEntity.run {
            val imageSearchMeta = kakaoImageSearchMeta.run {
                ImageSearchMeta(isEnd)
            }

            val imageDocumentList = kakaoImageDocuments.map {
                ImageDocument(
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

            ImageSearchResponse(imageSearchMeta, imageDocumentList)
        }
    }

}