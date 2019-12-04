package com.ch.yoon.imagesearch.presentation.favorite

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.image.ImageRepository
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.extension.removeFirstIf
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Creator : ch-yoon
 * Date : 2019-11-15.
 */
class FavoriteImagesViewModelTest {

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "알 수 없는 에러가 발생했습니다"
    }


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    private lateinit var mockApplication: Application
    @MockK
    private lateinit var mockImageRepository: ImageRepository

    private lateinit var favoriteImagesViewModel: FavoriteImagesViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        initApplication()
        initUtils()
        initFavoriteListViewModel()
    }

    private fun initApplication() {
        every { mockApplication.getString(R.string.unknown_error) } returns (UNKNOWN_ERROR_MESSAGE)
    }

    private fun initUtils() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns -1
    }

    private fun initFavoriteListViewModel() {
        favoriteImagesViewModel = FavoriteImagesViewModel(mockApplication, mockImageRepository)
    }

    @Test
    fun `좋아요 이미지 목록을 로드하는지 테스트`() {
        // given
        val publishFavoriteImageList = createFavoriteImageDocuments(3)
        every { mockImageRepository.getAllFavoriteImages() } returns Single.just(publishFavoriteImageList)

        // when
        favoriteImagesViewModel.loadFavoriteImageList()

        // then
        favoriteImagesViewModel.favoriteImageList.observeForever {
            assertEquals(publishFavoriteImageList, it)
        }
    }

    @Test
    fun `비어있는 이미지 목록을 로드하는지 테스트`() {
        // given
        val publishFavoriteImageList = createFavoriteImageDocuments(0)
        every { mockImageRepository.getAllFavoriteImages() } returns Single.just(publishFavoriteImageList)

        // when
        favoriteImagesViewModel.loadFavoriteImageList()

        // then
        favoriteImagesViewModel.favoriteImageList.observeForever {
            assertEquals(publishFavoriteImageList, it)
        }
    }

    @Test
    fun `이미지 목록 로드 에러 발생 시 에러 메시지가 반영되는지 테스트`() {
        // given
        every { mockImageRepository.getAllFavoriteImages() } returns Single.error(Exception())

        // when
        favoriteImagesViewModel.loadFavoriteImageList()

        // then
        favoriteImagesViewModel.showMessageEvent.observeForever {
            assertEquals(UNKNOWN_ERROR_MESSAGE, it)
        }
    }

    @Test
    fun `이미지 클릭 시 상세 화면으로 이동하는 이벤트 발생하는지 테스트`() {
        // when
        val clickedFavoriteImage = createFavoriteImageDocument(1)
        favoriteImagesViewModel.onClickImage(clickedFavoriteImage)

        // then
        favoriteImagesViewModel.moveToDetailScreenEvent.observeForever {
            assertEquals(clickedFavoriteImage, it)
        }
    }

    @Test
    fun `뒤로가기 버튼 클릭 시 종료 이벤트가 발생하는지 테스트`() {
        // when
        favoriteImagesViewModel.onClickBackPress()

        // then
        var finishCount = 0
        favoriteImagesViewModel.finishEvent.observeForever {
            finishCount ++
        }
        assertEquals(1, finishCount)
    }

    @Test
    fun `새로운 좋아요 이미지 이벤트 수신시 기존 목록에 추가되는지 테스트`() {
        // given
        val favoriteImageList = createFavoriteImageDocuments(3)
        every {
            mockImageRepository.getAllFavoriteImages()
        } returns Single.just(favoriteImageList)

        val favoritePublishSubject = PublishSubject.create<ImageDocument>()
        every {
            mockImageRepository.observeChangingFavoriteImage()
        } returns favoritePublishSubject

        // when
        val newFavoriteImage = createFavoriteImageDocument(4)
        favoriteImagesViewModel.loadFavoriteImageList()
        favoriteImagesViewModel.observeChangingFavoriteImage()
        favoritePublishSubject.onNext(newFavoriteImage)

        // then
        val expected = mutableListOf<ImageDocument>().apply {
            addAll(favoriteImageList)
            add(newFavoriteImage)
        }
        favoriteImagesViewModel.favoriteImageList.observeForever {
            assertEquals(expected, it)
        }
    }

    @Test
    fun `좋아요 취소 입네트 수신시 기존 목록에서 제거되는지 테스트`() {
        // given
        val favoriteImageList = createFavoriteImageDocuments(3)
        every {
            mockImageRepository.getAllFavoriteImages()
        } returns Single.just(favoriteImageList)

        val favoritePublishSubject = PublishSubject.create<ImageDocument>()
        every {
            mockImageRepository.observeChangingFavoriteImage()
        } returns favoritePublishSubject

        // when
        val noFavoriteImage = createNoFavoriteImageDocument(0)
        favoriteImagesViewModel.loadFavoriteImageList()
        favoriteImagesViewModel.observeChangingFavoriteImage()
        favoritePublishSubject.onNext(noFavoriteImage)

        // then
        val expected = mutableListOf<ImageDocument>().apply {
            addAll(favoriteImageList)
            removeFirstIf { it.id == noFavoriteImage.id }
        }
        favoriteImagesViewModel.favoriteImageList.observeForever {
            assertEquals(expected, it)
        }
    }

    private fun createFavoriteImageDocuments(size: Int): List<ImageDocument> {
        val list = mutableListOf<ImageDocument>()
        for(i in 0 until size) {
            list.add(createFavoriteImageDocument(i))
        }
        return list
    }

    private fun createFavoriteImageDocument(id: Int): ImageDocument {
        return createImageDocument(id, true)
    }

    private fun createNoFavoriteImageDocument(id: Int): ImageDocument {
        return createImageDocument(id, false)
    }

    private fun createImageDocument(id: Int, isFavorite: Boolean): ImageDocument {
        return ImageDocument(
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
}