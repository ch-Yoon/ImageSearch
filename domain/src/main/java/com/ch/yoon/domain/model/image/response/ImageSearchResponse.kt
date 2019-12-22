package com.ch.yoon.data.model.image.response

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
data class ImageSearchResponse(
    val imageSearchMeta: ImageSearchMeta,
    val imageDocuments: List<ImageDocument>
)