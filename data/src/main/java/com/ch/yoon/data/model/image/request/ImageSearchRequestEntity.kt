package com.ch.yoon.data.model.image.request


/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
data class ImageSearchRequestEntity(
    val keyword: String,
    val imageSortTypeEntity: ImageSortTypeEntity,
    val pageNumber: Int,
    val requiredSize: Int,
    val isFirstRequest: Boolean
)