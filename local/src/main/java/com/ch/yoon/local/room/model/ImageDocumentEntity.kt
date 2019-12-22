package com.ch.yoon.local.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Creator : ch-yoon
 * Date : 2019-11-12
 **/
@Entity(tableName = "imageDocumentEntities")
data class ImageDocumentEntity(
    @PrimaryKey val id: String,
    val collection: String,
    val thumbnailUrl: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val displaySiteName: String,
    val docUrl: String,
    val dateTime: String,
    val isFavorite: Boolean
)

