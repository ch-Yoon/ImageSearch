package com.ch.yoon.domain.model.image.request

import com.ch.yoon.data.model.image.request.ImageSortType


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