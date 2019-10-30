package com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.response

import com.google.gson.annotations.SerializedName

/**
 * Creator : ch-yoon
 * Date : 2019-10-29
 **/
data class KakaoImageDocument(
    @SerializedName("collection") val collection: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("display_sitename") val displaySiteName: String,
    @SerializedName("doc_url") val docUrl: String,
    @SerializedName("dateTime") val dateTime: String
)