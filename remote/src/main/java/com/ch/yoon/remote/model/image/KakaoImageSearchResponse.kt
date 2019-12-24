package com.ch.yoon.remote.model.image

import com.google.gson.annotations.SerializedName

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
data class KakaoImageSearchResponse(
    @SerializedName("meta") val kakaoImageSearchMeta: KakaoImageSearchMetaInfo,
    @SerializedName("documents") val kakaoImageDocuments: List<KakaoImageDocument>
)