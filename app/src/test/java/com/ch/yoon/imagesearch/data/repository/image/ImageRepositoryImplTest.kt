package com.ch.yoon.imagesearch.data.repository.image

import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.BaseRxTest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSearchRequest
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.data.repository.image.model.ImageSearchResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class ImageRepositoryImplTest : BaseRxTest() {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    private lateinit var mockImageLocalDataSource: ImageLocalDataSource
    @MockK
    private lateinit var mockImageRemoteDataSource: ImageRemoteDataSource

    private lateinit var imageRepository: ImageRepository

    override fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        imageRepository = ImageRepositoryImpl(mockImageLocalDataSource, mockImageRemoteDataSource)
    }

    override fun after() {
    }

    @Test
    fun `비어있는 이미지 목록을 반환하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.selectAllFavoriteImageDocumentList()
        } returns Single.just(emptyList())

        every {
            mockImageRemoteDataSource.requestImageList(any())
        } returns Single.just(ImageSearchResponse(mockk(), emptyList()))

        // when
        var result: List<ImageDocument>? = null
        imageRepository.requestImageList(emptyImageSearchRequest())
            .subscribe({
                result = it.imageDocumentList
            }, {
                result = null
            })
            .register()

        // then
        assertEquals(true, result?.isEmpty() ?: false)
    }

    @Test
    fun `좋아요가 반영된 이미지 리스트를 반환하는지 테스트`() {
        // given
        val favoriteList = createImageDocumentList(arrayOf("1", "3", "5"), true)
        every {
            mockImageLocalDataSource.selectAllFavoriteImageDocumentList()
        } returns Single.just(favoriteList)

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
            .register()

        // then
        val expectedList = createImageDocumentList(arrayOf("1", "2", "3", "4", "5", "6"), arrayOf("1", "3", "5"))
        assertEquals(expectedList, result)
    }

    @Test
    fun `이미지 목록 요청 에러 발생시 Repository Exception 반환하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.selectAllFavoriteImageDocumentList()
        } returns Single.just(emptyList())

        every {
            mockImageRemoteDataSource.requestImageList(any())
        } returns Single.error(RepositoryException.UnknownException(""))

        // when
        var exception: RepositoryException? = null
        imageRepository.requestImageList(emptyImageSearchRequest())
            .subscribe({
                exception = null
            }, {
                exception = if(it is RepositoryException) it else null
            })
            .register()

        // then
        assertEquals(true, exception is RepositoryException)
    }

    @Test
    fun `좋아요 저장 요청 성공 테스트`() {
        // given
        val favoriteDocument = createImageDocument("1", true)
        every {
            mockImageLocalDataSource.saveFavoriteImageDocument(favoriteDocument)
        } returns Completable.complete()

        // when
        var isSuccess = false
        imageRepository.saveFavoriteImage(favoriteDocument)
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
    fun `좋아요 저장 에러 발생 시 Repository Exception 반환하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.saveFavoriteImageDocument(any())
        } returns Completable.error(RepositoryException.UnknownException(""))

        // when
        var exception: RepositoryException? = null
        imageRepository.saveFavoriteImage( createImageDocument("1", true))
            .subscribe({
                exception = null
            }, {
                exception = if(it is RepositoryException) it else null
            })
            .register()

        // then
        assertEquals(true, exception is RepositoryException)
    }

    @Test
    fun `좋아요 취소 성공 테스트`() {
        // given
        val noFavoriteDocument = createImageDocument("1", false)
        every {
            mockImageLocalDataSource.deleteFavoriteImageDocument(noFavoriteDocument)
        } returns Completable.complete()

        // when
        var isSuccess = false
        imageRepository.deleteFavoriteImage(noFavoriteDocument)
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
    fun `좋아요 취소 에러 발생 시 Repository Exception 반환하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.deleteFavoriteImageDocument(any())
        } returns Completable.error(RepositoryException.UnknownException(""))

        // when
        var exception: RepositoryException? = null
        imageRepository.deleteFavoriteImage(createImageDocument("1", false))
            .subscribe({
                exception = null
            }, {
                exception = if(it is RepositoryException) it else null
            })
            .register()

        // then
        assertEquals(true, exception is RepositoryException)
    }

    @Test
    fun `좋아요 저장 성공 시 옵저빙 하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.saveFavoriteImageDocument(any())
        } returns Completable.complete()

        // when
        val actualList = mutableListOf<ImageDocument>()
        imageRepository.observeChangingFavoriteImage()
            .subscribe({ actualList.add(it) }, {})
            .register()

        val publishList = listOf(
            createImageDocument("1", true),
            createImageDocument("2", true),
            createImageDocument("3", true)
        )
        publishList.forEach {
            imageRepository.saveFavoriteImage(it)
                .subscribe({}, {})
                .register()
        }

        // then
        val expected = publishList
        assertEquals(expected, actualList)
    }

    @Test
    fun `저장 실패하는 좋아요 이미지를 옵저빙 안하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.saveFavoriteImageDocument(any())
        } returns Completable.error(RepositoryException.UnknownException(""))

        // when
        val actualList = mutableListOf<ImageDocument>()
        imageRepository.observeChangingFavoriteImage()
            .subscribe({ actualList.add(it) }, {})
            .register()

        val publishList = listOf(
            createImageDocument("1", true),
            createImageDocument("2", true),
            createImageDocument("3", true)
        )
        publishList.forEach {
            imageRepository.saveFavoriteImage(it)
                .subscribe({}, {})
                .register()
        }

        // then
        assertEquals(0, actualList.size)
    }

    @Test
    fun `좋아요 취소 성공 시 옵저빙 하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.deleteFavoriteImageDocument(any())
        } returns Completable.complete()

        // when
        val actualList = mutableListOf<ImageDocument>()
        imageRepository.observeChangingFavoriteImage()
            .subscribe({ actualList.add(it) }, {})
            .register()

        val publishList = listOf(
            createImageDocument("1", false),
            createImageDocument("2", false),
            createImageDocument("3", false)
        )

        publishList.forEach {
            imageRepository.deleteFavoriteImage(it)
                .subscribe({}, {})
                .register()
        }

        // then
        val expected = publishList
        assertEquals(expected, actualList)
    }

    @Test
    fun `취소 실패하는 좋아요 이미지를 옵저빙 안하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.deleteFavoriteImageDocument(any())
        } returns Completable.error(RepositoryException.UnknownException(""))

        // when
        val actualList = mutableListOf<ImageDocument>()
        imageRepository.observeChangingFavoriteImage()
            .subscribe({ actualList.add(it) }, {})
            .register()

        val publishList = listOf(
            createImageDocument("1", false),
            createImageDocument("2", false),
            createImageDocument("3", false)
        )
        publishList.forEach {
            imageRepository.deleteFavoriteImage(it)
                .subscribe({}, {})
                .register()
        }

        // then
        assertEquals(0, actualList.size)
    }

    //requestFavoriteImageList
    @Test
    fun `비어있는 좋아요 목록을 반환하는지 테스트`() {
        // given
        every {
            mockImageLocalDataSource.selectAllFavoriteImageDocumentList()
        } returns Single.just(emptyList())

        // when
        var list: List<ImageDocument>? = null
        imageRepository.requestFavoriteImageList()
            .subscribe({
                list = it
            }, {
                list = null
            })
            .register()

        // then
        assertEquals(0, list?.size ?: -1)
    }

    @Test
    fun `좋아요 목록을 반환하는지 테스트`() {
        // given
        val publishList = createImageDocumentList(arrayOf("1", "2", "3"), true)
        every {
            mockImageLocalDataSource.selectAllFavoriteImageDocumentList()
        } returns Single.just(publishList)

        // when
        var actualList: List<ImageDocument>? = null
        imageRepository.requestFavoriteImageList()
            .subscribe({
                actualList = it
            }, {
                actualList = null
            })
            .register()

        // then
        assertEquals(publishList, actualList)
    }

    private fun emptyImageSearchRequest(): ImageSearchRequest {
        return ImageSearchRequest("", ImageSortType.ACCURACY, 1, 1, true)
    }

    private fun createImageDocumentList(
        idArray: Array<String>,
        favoriteArray: Array<String>
    ): List<ImageDocument> {
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

    private fun createImageDocumentList(
        idArray: Array<String>,
        isFavorite: Boolean
    ): List<ImageDocument> {
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