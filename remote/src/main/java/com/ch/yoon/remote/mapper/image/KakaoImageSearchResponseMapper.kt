package com.ch.yoon.remote.mapper.image

import com.ch.yoon.data.model.image.response.ImageSearchResponseEntity
import com.ch.yoon.remote.mapper.EntityMapper
import com.ch.yoon.remote.model.image.KakaoImageSearchResponse

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
object KakaoImageSearchResponseMapper : EntityMapper<KakaoImageSearchResponse, ImageSearchResponseEntity> {

    override fun fromRemote(model: KakaoImageSearchResponse): ImageSearchResponseEntity {
        return model.run {
            val imageSearchMeta = KakaoImageSearchMetaMapper.fromRemote(kakaoImageSearchMeta)
            val imageDocumentList = kakaoImageDocuments.map { KakaoImageDocumentMapper.fromRemote(it) }
            ImageSearchResponseEntity(imageSearchMeta, imageDocumentList)
        }
    }
}