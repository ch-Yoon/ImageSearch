package com.ch.yoon.imagesearch.data.local.room.entity.mapper

import com.ch.yoon.local.entity.ImageDocumentEntity
import com.ch.yoon.data.model.image.ImageDocument
import org.junit.Test

import org.junit.Assert.*

/**
 * Creator : ch-yoon
 * Date : 2019-11-14
 */
class ImageDocumentEntityMapperTest {

    @Test
    fun `ImageDocument를 ImageDocumentEntity로 맵핑하는지 테스트`() {
        // given
        val imageDocument = createImageDocument("1")

        // when
        val imageDocumentEntity = com.ch.yoon.local.entity.mapper.ImageDocumentEntityMapper.toEntity(imageDocument)

        // then
        val expected = toEntity(imageDocument)
        assertEquals(expected, imageDocumentEntity)
    }

    @Test
    fun `ImageDocumentEntity를 ImageDocument로 맵핑하는지 테스트`() {
        // given
        val imageDocumentEntity = createImageDocumentEntity("1")

        // when
        val imageDocument = com.ch.yoon.local.entity.mapper.ImageDocumentEntityMapper.fromEntity(imageDocumentEntity)

        // then
        val expected = fromEntity(imageDocumentEntity)
        assertEquals(expected, imageDocument)
    }

    @Test
    fun `ImageDocumentEntityList를 ImageDocumentList로 맵핑하는지 테스트`() {
        // given
        val imageDocumentEntityList = mutableListOf(
            createImageDocumentEntity("1"),
            createImageDocumentEntity("2"),
            createImageDocumentEntity("3"),
            createImageDocumentEntity("4")
        )

        // when
        val imageDocumentList = com.ch.yoon.local.entity.mapper.ImageDocumentEntityMapper.fromEntityList(imageDocumentEntityList)

        // then
        val expected = imageDocumentEntityList.map { fromEntity(it) }
        assertEquals(expected, imageDocumentList)
    }

    private fun createImageDocument(id: String): com.ch.yoon.data.model.image.ImageDocument {
        return com.ch.yoon.data.model.image.ImageDocument(
            id,
            "collection$id",
            "thumbnailUrl$id",
            "imageUrl$id",
            0,
            0,
            "displaySiteName$id",
            "docUrl$id",
            "dateTime$id",
            true
        )
    }

    private fun createImageDocumentEntity(id: String): com.ch.yoon.local.entity.ImageDocumentEntity {
        return com.ch.yoon.local.entity.ImageDocumentEntity(
            id,
            "collection$id",
            "thumbnailUrl$id",
            "imageUrl$id",
            0,
            0,
            "displaySiteName$id",
            "docUrl$id",
            "dateTime$id",
            true
        )
    }

    private fun toEntity(imageDocument: com.ch.yoon.data.model.image.ImageDocument): com.ch.yoon.local.entity.ImageDocumentEntity {
        return imageDocument.run {
            com.ch.yoon.local.entity.ImageDocumentEntity(
                id,
                collection,
                thumbnailUrl,
                imageUrl,
                width,
                height,
                displaySiteName,
                docUrl,
                dateTime,
                isFavorite
            )
        }
    }

    private fun fromEntity(imageDocumentEntity: com.ch.yoon.local.entity.ImageDocumentEntity): com.ch.yoon.data.model.image.ImageDocument {
        return imageDocumentEntity.run {
            com.ch.yoon.data.model.image.ImageDocument(
                id,
                collection,
                thumbnailUrl,
                imageUrl,
                width,
                height,
                displaySiteName,
                docUrl,
                dateTime,
                isFavorite
            )
        }
    }

}