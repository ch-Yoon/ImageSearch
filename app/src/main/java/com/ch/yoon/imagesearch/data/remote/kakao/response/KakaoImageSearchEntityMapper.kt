package com.ch.yoon.imagesearch.data.remote.kakao.response

import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchMeta
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object KakaoImageSearchEntityMapper {

    fun toEntity(remoteEntity: KakaoImageSearchResponse): ImageSearchResponse {
        remoteEntity.run {
            val imageSearchMeta = kakaoImageSearchMeta.run {
                ImageSearchMeta(isEnd)
            }

            val imageDocumentList = kakaoImageDocumentList.map {
                ImageDocument(
                    "${it.thumbnailUrl}&${it.imageUrl}",
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

            return ImageSearchResponse(imageSearchMeta, imageDocumentList)
        }
    }

}