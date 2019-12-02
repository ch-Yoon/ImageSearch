package com.ch.yoon.imagesearch.data.local.room

import com.ch.yoon.imagesearch.BaseRxTest
import com.ch.yoon.imagesearch.data.local.room.dao.ImageDAO
import com.ch.yoon.imagesearch.data.local.room.entity.ImageDocumentEntity
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.imagesearch.data.repository.image.ImageLocalDataSource
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.Exception

/**
 * Creator : ch-yoon
 * Date : 2019-11-14
 */
class ImageLocalDataSourceImplTest : BaseRxTest() {

    @MockK
    lateinit var mockImageDAO: ImageDAO

    private lateinit var imageLocalDataSource: ImageLocalDataSource

    override fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        imageLocalDataSource = ImageLocalDataSourceImpl(mockImageDAO)
    }

    override fun after() {
    }

    @Test
    fun `좋아요 저장 성공하는지 테스트`() {
        // given
        every { mockImageDAO.insertOrUpdateImageDocument(any()) } returns Completable.complete()

        // when
        var isSuccess = false
        imageLocalDataSource.saveFavoriteImageDocument(createImageDocument("1"))
            .subscribe({
                isSuccess = true
            }, {
                isSuccess = false
            })
            .register()

        // then
        assertEquals(true, isSuccess)
    }

    @Test
    fun `좋아요 저장 에러 발생 시 RepositoryException 발생하는지 테스트`() {
        // given
        every {
            mockImageDAO.insertOrUpdateImageDocument(any())
        } returns Completable.error(Exception())

        // when
        var actualException: RepositoryException? = null
        imageLocalDataSource.saveFavoriteImageDocument(createImageDocument("1"))
            .subscribe({
                actualException = null
            }, {
                actualException = if(it is RepositoryException) it else null
            })
            .register()

        // then
        assertEquals(true, actualException is RepositoryException)
    }

    @Test
    fun `좋아요 삭제 성공하는지 테스트`() {
        // given
        every { mockImageDAO.deleteImageDocument(any()) } returns Completable.complete()

        // when
        var isSuccess = false
        imageLocalDataSource.deleteFavoriteImageDocument(createImageDocument("1"))
            .subscribe({
                isSuccess = true
            }, {
                isSuccess = false
            })
            .register()

        // then
        assertEquals(true, isSuccess)
    }

    @Test
    fun `좋아요 삭제 에러 발생시 Repository Exception 으로 반환하는지 테스트`() {
        // given
        every { mockImageDAO.deleteImageDocument(any()) } returns Completable.error(Exception())

        // when
        var actualException: RepositoryException? = null
        imageLocalDataSource.deleteFavoriteImageDocument(createImageDocument("1"))
            .subscribe({
                actualException = null
            }, {
                actualException = if(it is RepositoryException) it else null
            })
            .register()

        // then
        assertEquals(true, actualException is RepositoryException)
    }

    @Test
    fun `저장되어 있는 모든 좋아요 목록을 반환하는지 테스트`() {
        // given
        val entityList = createImageDocumentEntityList(arrayOf("1", "2", "3", "4"))
        every { mockImageDAO.selectAllImageDocument() } returns Single.just(entityList)

        // when
        var actualList: List<ImageDocument>? = null
        imageLocalDataSource.getAllFavoriteImages()
            .subscribe({
                actualList = it
            }, {
                actualList = null
            })

        // then
        val expected = fromEntityList(entityList)
        assertEquals(expected, actualList)
    }

    @Test
    fun `비어있는 좋아요 목록을 반환하는지 테스트`() {
        // given
        every { mockImageDAO.selectAllImageDocument() } returns Single.just(listOf())

        // when
        var actualList: List<ImageDocument>? = null
        imageLocalDataSource.getAllFavoriteImages()
            .subscribe({
                actualList = it
            }, {
                actualList = null
            })

        // then
        assertEquals(0, actualList?.size ?: -1)
    }

    @Test
    fun `모든 좋아요 목록 검색 에러 발생 시 Repository Exception으로 반환하는지 테스트`() {
        // given
        every { mockImageDAO.selectAllImageDocument() } returns Single.error(Exception())

        // when
        var actualException: RepositoryException? = null
        imageLocalDataSource.getAllFavoriteImages()
            .subscribe({
                actualException = null
            }, {
                actualException = if(it is RepositoryException) it else null
            })

        // then
        assertEquals(true, actualException is RepositoryException)
    }

    private fun fromEntityList(list: List<ImageDocumentEntity>): List<ImageDocument> {
        return list.map { fromEntity(it) }
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

    private fun createImageDocumentEntityList(idArray: Array<String>): List<ImageDocumentEntity> {
        return mutableListOf<ImageDocumentEntity>().apply {
            for(id in idArray) {
                add(createImageDocumentEntity(id))
            }
        }
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

}