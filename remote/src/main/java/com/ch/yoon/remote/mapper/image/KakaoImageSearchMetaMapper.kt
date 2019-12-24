package com.ch.yoon.remote.mapper.image

import com.ch.yoon.data.model.image.response.ImageSearchMetaEntity
import com.ch.yoon.remote.mapper.EntityMapper
import com.ch.yoon.remote.model.image.KakaoImageSearchMetaInfo

/**
 * Creator : ch-yoon
 * Date : 2019-12-23.
 */
object KakaoImageSearchMetaMapper : EntityMapper<KakaoImageSearchMetaInfo, ImageSearchMetaEntity> {

    override fun fromRemote(model: KakaoImageSearchMetaInfo): ImageSearchMetaEntity {
        return model.run {
            ImageSearchMetaEntity(isEnd)
        }
    }
}