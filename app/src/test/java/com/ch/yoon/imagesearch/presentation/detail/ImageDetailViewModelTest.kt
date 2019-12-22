package com.ch.yoon.imagesearch.presentation.detail

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Rule
import android.text.TextUtils
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.image.ImageRepository
import com.ch.yoon.data.model.image.ImageDocument
import io.mockk.verify
import io.reactivex.Completable
import junit.framework.Assert.assertEquals
import org.junit.Test


/**
 * Creator : ch-yoon
 * Date : 2019-11-02.
 */
class ImageDetailViewModelTest {

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "알 수 없는 에러가 발생했습니다"
        private const val NONEXISTENT_URL_MESSAGE = "연결되는 사이트가 없습니다"
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    private lateinit var mockApplication: Application
    @MockK
    private lateinit var mockImageRepository: ImageRepository

    private lateinit var imageDetailViewModel: ImageDetailViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        initApplication()
        initImageDetailViewModel()
        initUtils()
    }

    private fun initApplication() {
        every { mockApplication.getString(R.string.unknown_error) } returns (UNKNOWN_ERROR_MESSAGE)
        every { mockApplication.getString(R.string.non_existent_url_error) } returns (NONEXISTENT_URL_MESSAGE)
    }

    private fun initImageDetailViewModel() {
        imageDetailViewModel = ImageDetailViewModel(mockApplication, mockImageRepository)
    }

    private fun initUtils() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns -1

        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } answers {
            invocation.args[0]?.let { value ->
                if(value is String) {
                    value.length == 0
                } else {
                    false
                }
            } ?: false
        }
    }

    @Test
    fun `이미지 상세정보 값이 null일때 에러메시지가 반영되는지 테스트`() {
        // when
        imageDetailViewModel.showImageDetailInfo(null)

        // then
        imageDetailViewModel.showMessageEvent.observeForever { message ->
            assertEquals(message, UNKNOWN_ERROR_MESSAGE)
        }
    }

    @Test
    fun `이미지 상세정보 null일때 종료 이벤트가 호출되는지 테스트`() {
        // when
        imageDetailViewModel.showImageDetailInfo(null)

        // then
        var count = 0
        imageDetailViewModel.finishEvent.observeForever { count++ }
        assertEquals(1, count)
    }

    @Test
    fun `이미지 상세정보의 imageUrl이 반영되는지 테스트`() {
        // given
        val imageDocument = createVirtualImageDocument(1)

        // when
        imageDetailViewModel.showImageDetailInfo(imageDocument)

        // then
        imageDetailViewModel.imageUrlInfo.observeForever { imageUrl ->
            assertEquals(imageDocument.imageUrl, imageUrl)
        }
    }

    @Test
    fun `웹 이동 버튼 클릭시 웹 이동 이벤트가 발생되는지 테스트`() {
        // given
        val imageDocument = createVirtualImageDocument(1)

        // when
        var moveWebEventCount = 0
        imageDetailViewModel.moveToWebEvent.observeForever { moveWebEventCount++ }
        imageDetailViewModel.showImageDetailInfo(imageDocument)
        imageDetailViewModel.onClickWebButton()

        // then
        assertEquals(1, moveWebEventCount)
    }

    @Test
    fun `웹 이동 버튼 클릭시 웹 주소가 없다면 에러메시지가 반영되는지 테스트`() {
        // given
        val imageDocument = createVirtualImageDocumentWithEmptyDocUrl(1)

        // when
        imageDetailViewModel.showImageDetailInfo(imageDocument)
        imageDetailViewModel.onClickWebButton()

        // then
        imageDetailViewModel.showMessageEvent.observeForever { message ->
            assertEquals(NONEXISTENT_URL_MESSAGE, message)
        }
    }

    @Test
    fun `좋아요 이미지일 경우 좋아요 상태가 반영되는지 테스트`() {
        // when
        val favoriteImageDocument = createVirtualImageDocument(1, true)
        imageDetailViewModel.showImageDetailInfo(favoriteImageDocument)

        // then
        imageDetailViewModel.isFavorite.observeForever {
            assertEquals(true, it)
        }
    }

    @Test
    fun `좋아요 이미지가 아닐 경우 좋아요 아닌 상태가 반영되는지 테스트`() {
        // when
        val favoriteImageDocument = createVirtualImageDocument(1, false)
        imageDetailViewModel.showImageDetailInfo(favoriteImageDocument)

        // then
        imageDetailViewModel.isFavorite.observeForever {
            assertEquals(false, it)
        }
    }

    @Test
    fun `좋아요가 선택되어 있을 때 좋아요 버튼 클릭 시 repository 에 삭제 요청을 하는지 테스트`() {
        // given
        every { mockImageRepository.deleteFavoriteImage(any()) } returns Completable.complete()

        // when
        val favoriteImageDocument = createVirtualImageDocument(1, true)
        imageDetailViewModel.showImageDetailInfo(favoriteImageDocument)
        imageDetailViewModel.onClickFavorite()

        // then
        verify(exactly = 1) {
            mockImageRepository.deleteFavoriteImage(any())
        }
    }

    @Test
    fun `좋아요가 선택되어 있을 때 좋아요 버튼 클릭 시 좋아요가 해제되는지 repository 에 삭제 요청을 하는지 테스트`() {
        // given
        every { mockImageRepository.deleteFavoriteImage(any()) } returns Completable.complete()

        // when
        val favoriteImageDocument = createVirtualImageDocument(1, true)
        imageDetailViewModel.showImageDetailInfo(favoriteImageDocument)
        imageDetailViewModel.onClickFavorite()

        // then
        imageDetailViewModel.isFavorite.observeForever {
            assertEquals(false, it)
        }
    }

    @Test
    fun `좋아요가 선택되어있지 않을 때 좋아요 버튼 클릭 시 repository에 저장 요청을 하는지 테스트`() {
        // given
        every { mockImageRepository.saveFavoriteImage(any()) } returns Completable.complete()

        // when
        val favoriteImageDocument = createVirtualImageDocument(1, false)
        imageDetailViewModel.showImageDetailInfo(favoriteImageDocument)
        imageDetailViewModel.onClickFavorite()

        // then
        verify(exactly = 1) {
            mockImageRepository.saveFavoriteImage(any())
        }
    }

    @Test
    fun `좋아요가 선택되어있지 않을 때 좋아요 버튼 클릭 시 좋아요가 반영되는지 테스트`() {
        // given
        every { mockImageRepository.saveFavoriteImage(any()) } returns Completable.complete()

        // when
        val noFavoriteImageDocument = createVirtualImageDocument(1, false)
        imageDetailViewModel.showImageDetailInfo(noFavoriteImageDocument)
        imageDetailViewModel.onClickFavorite()

        // then
        imageDetailViewModel.isFavorite.observeForever {
            assertEquals(true, it)
        }
    }

    private fun createVirtualImageDocument(id: Int, isFavorite: Boolean = false): com.ch.yoon.data.model.image.ImageDocument {
        return com.ch.yoon.data.model.image.ImageDocument(
            "id$id",
            "collection$id",
            "thumbnailUrl$id",
            "imageUrlInfo$id",
            0,
            0,
            "displaySiteName$id",
            "docUrl$id",
            "dateTime$id",
            isFavorite
        )
    }

    private fun createVirtualImageDocumentWithEmptyDocUrl(id: Int): com.ch.yoon.data.model.image.ImageDocument {
        return com.ch.yoon.data.model.image.ImageDocument(
            "id$id",
            "collection$id",
            "thumbnailUrl$id",
            "imageUrlInfo$id",
            0,
            0,
            "displaySiteName$id",
            "",
            "dateTime$id",
            false
        )
    }
}