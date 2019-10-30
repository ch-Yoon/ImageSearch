package com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.request

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
data class ImageSearchRequest(
    val keyword: String,
    val imageSortType: ImageSortType,
    val pageNumber: Int,
    val requiredSize: Int,
    val isFirstRequest: Boolean
)