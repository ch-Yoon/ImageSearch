package com.ch.yoon.imagesearch.data.local.room.entity.mapper

import com.ch.yoon.imagesearch.data.local.room.entity.ImageDocumentEntity
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
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
        val imageDocumentEntity = ImageDocumentEntityMapper.toEntity(imageDocument)

        // then
        val expected = toEntity(imageDocument)
        assertEquals(expected, imageDocumentEntity)
    }

    @Test
    fun `ImageDocumentEntity를 ImageDocument로 맵핑하는지 테스트`() {
        // given
        val imageDocumentEntity = createImageDocumentEntity("1")

        // when
        val imageDocument = ImageDocumentEntityMapper.fromEntity(imageDocumentEntity)

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
        val imageDocumentList = ImageDocumentEntityMapper.fromEntityList(imageDocumentEntityList)

        // then
        val expected = imageDocumentEntityList.map { fromEntity(it) }
        assertEquals(expected, imageDocumentList)
    }

    private fun createImageDocument(id: String): ImageDocument {
        return ImageDocument(
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

    private fun createImageDocumentEntity(id: String): ImageDocumentEntity {
        return ImageDocumentEntity(
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

    private fun toEntity(imageDocument: ImageDocument): ImageDocumentEntity {
        return imageDocument.run {
            ImageDocumentEntity(
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

    private fun fromEntity(imageDocumentEntity: ImageDocumentEntity): ImageDocument {
        return imageDocumentEntity.run {
            ImageDocument(
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