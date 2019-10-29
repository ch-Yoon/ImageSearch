package com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.response

import com.google.gson.annotations.SerializedName

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
data class ImageSearchResponse(
    @SerializedName("meta") val searchMetaInfo: SearchMetaInfo,
    @SerializedName("documents") val imageDocumentList: List<ImageDocument>
)