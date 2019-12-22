package com.ch.yoon.remote.kakao.model.image.mapper

import com.ch.yoon.data.model.image.response.ImageSearchMetaEntity
import com.ch.yoon.remote.kakao.model.image.response.KakaoImageSearchMetaInfo

/**
 * Creator : ch-yoon
 * Date : 2019-12-23.
 */
object KakaoImageSearchMetaMapper : EntityMapper<KakaoImageSearchMetaInfo, ImageSearchMetaEntity> {

    override fun toEntity(remoteModel: KakaoImageSearchMetaInfo): ImageSearchMetaEntity {
        return remoteModel.run {
            ImageSearchMetaEntity(isEnd)
        }
    }
}