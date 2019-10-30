package com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.response

import com.google.gson.annotations.SerializedName

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
data class KakaoImageSearchResponse(
    @SerializedName("meta") val kakaoImageSearchMeta: KakaoImageSearchMetaInfo,
    @SerializedName("documents") val kakaoImageDocumentList: List<KakaoImageDocument>
)