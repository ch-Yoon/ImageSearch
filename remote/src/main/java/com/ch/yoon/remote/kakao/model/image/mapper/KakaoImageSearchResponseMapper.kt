package com.ch.yoon.remote.kakao.model.image.mapper

import com.ch.yoon.data.model.image.response.ImageSearchResponseEntity
import com.ch.yoon.remote.kakao.model.image.response.KakaoImageSearchResponse

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object KakaoImageSearchResponseMapper : EntityMapper<KakaoImageSearchResponse, ImageSearchResponseEntity> {

    override fun toEntity(remoteModel: KakaoImageSearchResponse): ImageSearchResponseEntity {
        return remoteModel.run {
            val imageSearchMeta = KakaoImageSearchMetaMapper.toEntity(kakaoImageSearchMeta)
            val imageDocumentList = kakaoImageDocuments.map { KakaoImageDocumentMapper.toEntity(it) }
            ImageSearchResponseEntity(imageSearchMeta, imageDocumentList)
        }
    }
}