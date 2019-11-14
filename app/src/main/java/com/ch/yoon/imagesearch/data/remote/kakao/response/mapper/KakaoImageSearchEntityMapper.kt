package com.ch.yoon.imagesearch.data.remote.kakao.response.mapper

import com.ch.yoon.imagesearch.data.remote.kakao.response.KakaoImageSearchResponse
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchMeta
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse

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

            val imageDocumentList = kakaoImageDocumentList.map {
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