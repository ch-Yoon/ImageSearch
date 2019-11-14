package com.ch.yoon.imagesearch.data.repository.image

import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class ImageRepositoryImplTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    private lateinit var mockImageLocalDataSource: ImageLocalDataSource
    @MockK
    private lateinit var mockImageRemoteDataSource: ImageRemoteDataSource

    private lateinit var imageRepository: ImageRepository

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        imageRepository = ImageRepositoryImpl(mockImageLocalDataSource, mockImageRemoteDataSource)
    }

    @Test
    fun `좋아요가 반영된 이미지 리스트를 반환하는지 테스트`() {
        // given
        val favoriteList = createImageDocumentList(arrayOf("1", "3", "5"), true)
        every { mockImageLocalDataSource.selectAllFavoriteImageDocumentList() } returns Single.just(favoriteList)

        val noFavoriteList = createImageDocumentList(arrayOf("1", "2", "3", "4", "5", "6"), false)
        val response = ImageSearchResponse(mockk(), noFavoriteList)
        every { mockImageRemoteDataSource.requestImageList(any()) } returns Single.just(response)

        // when
        var result: List<ImageDocument>? = null
        imageRepository.requestImageList(emptyImageSearchRequest())
            .subscribe({
                result = it.imageDocumentList
            }, {
                result = null
            })

        // then
        val expectedList = createImageDocumentList(arrayOf("1", "2", "3", "4", "5", "6"), arrayOf("1", "3", "5"))
        assertEquals(expectedList, result)
    }

    @Test
    fun `좋아요 저장 요청을 local에 전달하는지 테스트`() {
        // given
        val favoriteDocument = createImageDocument("1", true)
        every { mockImageLocalDataSource.saveFavoriteImageDocument(favoriteDocument) } returns Completable.complete()

        // when
        imageRepository.saveFavoriteImage(favoriteDocument)

        // then
        verify(exactly = 1) { mockImageLocalDataSource.saveFavoriteImageDocument(favoriteDocument) }
    }

    @Test
    fun `좋아요 취소 요청을 local에 전달하는지 테스트`() {
        // given
        val noFavoriteDocument = createImageDocument("1", false)
        every { mockImageLocalDataSource.deleteFavoriteImageDocument(noFavoriteDocument) } returns Completable.complete()

        // when
        imageRepository.deleteFavoriteImage(noFavoriteDocument)

        // then
        verify(exactly = 1) { mockImageLocalDataSource.deleteFavoriteImageDocument(noFavoriteDocument) }
    }

   private fun emptyImageSearchRequest(): ImageSearchRequest {
        return ImageSearchRequest("", ImageSortType.ACCURACY, 1, 1, true)
    }

    private fun createImageDocumentList(idArray: Array<String>, favoriteArray: Array<String>): List<ImageDocument> {
        return mutableListOf<ImageDocument>().apply {
            val favoriteSet = favoriteArray.toSet()
            for(id in idArray) {
                if(favoriteSet.contains(id)) {
                    add(createImageDocument(id, true))
                } else {
                    add(createImageDocument(id, false))
                }
            }
        }
    }

    private fun createImageDocumentList(idArray: Array<String>, isFavorite: Boolean): List<ImageDocument> {
        return mutableListOf<ImageDocument>().apply {
            for(id in idArray) {
                add(createImageDocument(id, isFavorite))
            }
        }
    }

    private fun createImageDocument(id: String, isFavorite: Boolean): ImageDocument {
        return ImageDocument(
            id,
            "collection",
            "thumbnailUrl",
            "imageUrlInfo",
            0,
            0,
            "displaySiteName",
            "docUrl",
            "dateTime",
            isFavorite
        )
    }
}