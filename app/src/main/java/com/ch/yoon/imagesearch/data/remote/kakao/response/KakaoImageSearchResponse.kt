package com.ch.yoon.imagesearch.data.remote.kakao.response

import com.google.gson.annotations.SerializedName

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
data class KakaoImageSearchResponse(
    @SerializedName("meta") val kakaoImageSearchMeta: KakaoImageSearchMetaInfo,
    @SerializedName("documents") val kakaoImageDocuments: List<KakaoImageDocument>
)