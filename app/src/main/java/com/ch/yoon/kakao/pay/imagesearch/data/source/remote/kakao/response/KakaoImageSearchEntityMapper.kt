package com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.response

import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.ImageSearchMeta
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.model.ImageSearchResponse

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
                ImageDocument(it.thumbnailUrl, it.imageUrl, it.docUrl)
            }

            return ImageSearchResponse(imageSearchMeta, imageDocumentList)
        }
    }

}