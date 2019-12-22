package com.ch.yoon.imagesearch.presentation.search.imagesearch

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.R
import com.ch.yoon.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.data.repository.image.ImageRepository
import com.ch.yoon.data.model.error.RepositoryException
import com.ch.yoon.data.model.image.ImageDocument
import com.ch.yoon.data.model.image.ImageSearchMeta
import com.ch.yoon.data.model.image.ImageSearchResponse
import com.ch.yoon.imagesearch.presentation.common.pageload.PageLoadHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-01.
 */
class ImageSearchViewModelTest {

    companion object {
        private const val SUCCESS_NO_RESULT = "검색 결과가 없습니다"
        private const val SUCCESS_LAST_DATA = "마지막 검색 페이지입니다"
        private const val ERROR_NETWORK = "인터넷 연결을 확인해주세요"
        private const val ERROR_UNKNOWN_ERROR = "알 수 없는 에러가 발생했습니다"
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    lateinit var mockApplication: Application
    @MockK
    lateinit var mockRepository: ImageRepository
    @MockK
    lateinit var mockPageLoadHelper: PageLoadHelper<String>

    private lateinit var imageSearchViewModel: ImageSearchViewModel
    private var capturedPageLoadApprove: ((key: String, pageNumber: Int, dataSize: Int, isFirstPage: Boolean) -> Unit)? = null
    private val changedFavoriteImagesSubject = PublishSubject.create<com.ch.yoon.data.model.image.ImageDocument>()

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        initApplication()
        initUtils()
        initImageListViewModel()
    }

    private fun initApplication() {
        every { mockApplication.getString(R.string.success_image_search_no_result) } returns (SUCCESS_NO_RESULT)
        every { mockApplication.getString(R.string.success_image_search_last_data) } returns (SUCCESS_LAST_DATA)
        every { mockApplication.getString(R.string.network_not_connecting_error) } returns (ERROR_NETWORK)
        every { mockApplication.getString(R.string.unknown_error) } returns (ERROR_UNKNOWN_ERROR)
    }

    private fun initImageListViewModel() {
        val lambdaSlot = slot<(key: String, pageNumber: Int, dataSize: Int, isFirstPage: Boolean) -> Unit>()
        every {
            mockPageLoadHelper.onPageLoadApproveCallback = capture(lambdaSlot)
        } answers {
            capturedPageLoadApprove = lambdaSlot.captured
        }

        every {
            mockRepository.observeChangingFavoriteImage()
        } returns changedFavoriteImagesSubject

        imageSearchViewModel = ImageSearchViewModel(mockApplication, mockRepository, mockPageLoadHelper)
    }

    private fun initUtils() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns -1
    }

    @Test
    fun `한줄에 표현되는 아이템의 갯수를 변경하면 반영되는지_테스트`() {
        // when
        imageSearchViewModel.changeCountOfItemInLine(2)
        imageSearchViewModel.changeCountOfItemInLine(4)

        // then
        imageSearchViewModel.countOfItemInLine.observeForever { count ->
            assertEquals(4, count)
        }
    }

    @Test
    fun `최초 이미지 목록 요청시 수신한 이미지 목록이 반영되는지 테스트`() {
        // given
        val expect = createImageSearchResponse(3, true)
        every { mockRepository.getImages(any()) } returns (Single.just(expect))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageSearchViewModel.loadImages("테스트")

        // then
        imageSearchViewModel.imageDocuments.observeForever { actuallyList ->
            assertEquals(expect.imageDocuments, actuallyList)
        }
    }

    @Test
    fun `최초 이미지 목록 요청시 수신한 리스트의 사이즈가 0이어도 반영되는지 테스트`() {
        // given
        val expect = createImageSearchResponse(0, true)
        every { mockRepository.getImages(any()) } returns (Single.just(expect))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageSearchViewModel.loadImages("테스트")

        // then
        imageSearchViewModel.imageDocuments.observeForever { actuallyList ->
            assertEquals(0, actuallyList.size)
        }
    }

    @Test
    fun `최초 이미지 목록 요청시 수신한 리스트의 사이즈가 0이라면 검색결과가 없다는 메시지가 반영되는지 테스트`() {
        // given
        val expect = createImageSearchResponse(0, true)
        every { mockRepository.getImages(any()) } returns Single.just(expect)
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageSearchViewModel.loadImages("테스트")

        // then
        imageSearchViewModel.showMessageEvent.observeForever { message ->
            assertEquals(SUCCESS_NO_RESULT, message)
        }
    }

    @Test
    fun `최초 이미지 목록 요청시 네트워크 연결 에러가 수신된다면 에러 메시지가 반영되는지 테스트`() {
        // given
        every { mockRepository.getImages(any()) } returns (Single.error(RepositoryException.NetworkNotConnectingException("network error")))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageSearchViewModel.loadImages("테스트")

        // then
        imageSearchViewModel.showMessageEvent.observeForever { message ->
            assertEquals(message, ERROR_NETWORK)
        }
    }

    @Test
    fun `마지막 데이터라면 마지막을 뜻하는 메시지가 반영되는지 테스트`() {
        // given
        every { mockRepository.getImages(any()) } returns (Single.just(createImageSearchResponse(1, true)))

        // when
        imageSearchViewModel.loadImages("테스트");

        // then
        imageSearchViewModel.showMessageEvent.observeForever { message ->
            assertEquals(message, SUCCESS_LAST_DATA)
        }
    }

    @Test
    fun `추가 이미지 목록 요청시 수신된 추가 이미지 목록이 반영되는지 테스트`() {
        // given
        val firstValue = createImageSearchResponse(3, false)
        val secondValue = createImageSearchResponse(3, true)
        val expectedList = mutableListOf<com.ch.yoon.data.model.image.ImageDocument>().apply {
            addAll(firstValue.imageDocuments)
            addAll(secondValue.imageDocuments)
        }

        var isFirst = true
        every { mockRepository.getImages(any()) } returns if(isFirst) {
            isFirst = false
            Single.just(firstValue)
        } else {
            Single.just(secondValue)
        }
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, true) }
        every { mockPageLoadHelper.requestPreloadIfPossible(any(), any(), any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }


        // when
        imageSearchViewModel.loadImages("테스트")
        imageSearchViewModel.loadMoreImagesIfPossible(3)

        // then
        imageSearchViewModel.imageDocuments.observeForever { actuallyList ->
            assertEquals(expectedList, actuallyList)
        }
    }

    @Test
    fun `추가 이미지 목록 요청시 마지막 데이터였다면 레파지토리에 요청을 안하는지 테스트`() {
        // given
        every { mockRepository.getImages(any()) } returns (Single.just(createImageSearchResponse(1, true)))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, true) }
        every { mockPageLoadHelper.requestPreloadIfPossible(any(), any(), any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageSearchViewModel.loadImages("테스트")
        imageSearchViewModel.loadMoreImagesIfPossible(1)
        imageSearchViewModel.loadMoreImagesIfPossible(1)
        imageSearchViewModel.loadMoreImagesIfPossible(1)

        // then
        verify(exactly = 1) { mockRepository.getImages(any()) } // 최초 1번
    }

    @Test
    fun `재시도 요청시 레파지토리에 요청을 하는지 테스트`() {
        // given
        every { mockRepository.getImages(any()) } returns (Single.just(createImageSearchResponse(1, true)))
        every { mockPageLoadHelper.requestRetryAsPreviousValue() } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageSearchViewModel.retryLoadMoreImageList()

        //then
        verify(exactly = 1) { mockRepository.getImages(any()) }
    }

    @Test
    fun `이미지 검색 타입 변경시 반영되는지 테스트`() {
        // given
        every { mockRepository.getImages(any()) } returns (Single.just(createImageSearchResponse(1, true)))
        every { mockPageLoadHelper.getCurrentKey() } returns "key"
        // when
        val typeList = mutableListOf<com.ch.yoon.remote.kakao.request.ImageSortType>()
        imageSearchViewModel.imageSortType.observeForever { type ->
            typeList.add(type)
        }
        imageSearchViewModel.changeImageSortType(com.ch.yoon.remote.kakao.request.ImageSortType.ACCURACY)
        imageSearchViewModel.changeImageSortType(com.ch.yoon.remote.kakao.request.ImageSortType.RECENCY)

        // then
        assertEquals(com.ch.yoon.remote.kakao.request.ImageSortType.ACCURACY, typeList[0]) // 최초 default 로 설정되어 있는 값
        assertEquals(com.ch.yoon.remote.kakao.request.ImageSortType.ACCURACY, typeList[1])
        assertEquals(com.ch.yoon.remote.kakao.request.ImageSortType.RECENCY, typeList[2])
    }

    @Test
    fun `이미지 클릭 시 이미지 상세 화면 이동 이벤트 호출되는지 테스트`() {
        // given
        val imageDocument = createImageDocument("0")

        // when
        imageSearchViewModel.onClickImage(imageDocument)

        // then
        imageSearchViewModel.moveToDetailScreenEvent.observeForever { receivedImageDocument ->
            assertEquals(imageDocument, receivedImageDocument)
        }
    }

    @Test
    fun `새로운 좋아요 이벤트 수신시 기존 목록에 반영되는지 테스트`() {
        // given
        every {
            mockPageLoadHelper.requestFirstLoad(any())
        } answers {
            capturedPageLoadApprove?.invoke("", 1, 1, true)
        }

        val imageResponse = createImageSearchResponse(3, true)
        imageResponse.imageDocuments.forEach { it.isFavorite = false }
        every { mockRepository.getImages(any()) } returns Single.just(imageResponse)

        // when
        imageSearchViewModel.loadImages("테스트")

        val noFavoriteImage = createImageDocument(imageResponse.imageDocuments[0].id, true)
        changedFavoriteImagesSubject.onNext(noFavoriteImage)

        // then
        val expected = createImageSearchResponse(3, true).imageDocuments.toMutableList()
        expected.forEach { it.isFavorite = false }
        expected[0].isFavorite = true
        imageSearchViewModel.imageDocuments.observeForever {
            assertEquals(expected.toList(), it)
        }
    }

    @Test
    fun `새로운 좋아요 취소 이벤트 수신시 기존 목록에 반영되는지 테스트`() {
        // given
        every {
            mockPageLoadHelper.requestFirstLoad(any())
        } answers {
            capturedPageLoadApprove?.invoke("", 1, 1, true)
        }

        val imageResponse = createImageSearchResponse(3, true)
        imageResponse.imageDocuments.forEach { it.isFavorite = true }
        every { mockRepository.getImages(any()) } returns Single.just(imageResponse)

        // when
        imageSearchViewModel.loadImages("테스트")

        val noFavoriteImage = createImageDocument(imageResponse.imageDocuments[0].id, false)
        changedFavoriteImagesSubject.onNext(noFavoriteImage)

        // then
        val expected = createImageSearchResponse(3, true).imageDocuments.toMutableList()
        expected.forEach { it.isFavorite = true }
        expected[0].isFavorite = false
        imageSearchViewModel.imageDocuments.observeForever {
            assertEquals(expected.toList(), it)
        }
    }

    private fun createImageSearchResponse(
        documentListSize: Int,
        isLastData: Boolean
    ): com.ch.yoon.data.model.image.ImageSearchResponse {
        val searchMeta = com.ch.yoon.data.model.image.ImageSearchMeta(isLastData)
        val imageDocumentList = createImageDocumentList(documentListSize)
        return com.ch.yoon.data.model.image.ImageSearchResponse(searchMeta, imageDocumentList)
    }

    private fun createImageDocumentList(size: Int): MutableList<com.ch.yoon.data.model.image.ImageDocument> {
        val list = mutableListOf<com.ch.yoon.data.model.image.ImageDocument>()
        for(i in 0 until size) {
            list.add(createImageDocument(i.toString()))
        }
        return list
    }

    private fun createImageDocument(id: String, isFavorite: Boolean = false): com.ch.yoon.data.model.image.ImageDocument {
        return com.ch.yoon.data.model.image.ImageDocument(
            id,
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