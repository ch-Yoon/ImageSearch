package com.ch.yoon.data.model.image.response

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
data class ImageSearchResponseEntity(
    val imageSearchMetaEntity: ImageSearchMetaEntity,
    val imageDocumentEntities: List<ImageDocumentEntity>
)