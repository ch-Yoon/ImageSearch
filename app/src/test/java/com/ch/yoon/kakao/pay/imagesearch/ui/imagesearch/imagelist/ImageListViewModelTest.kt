package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository
import com.ch.yoon.kakao.pay.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageSearchMeta
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.ImageSearchResponse
import com.ch.yoon.kakao.pay.imagesearch.ui.common.pageload.PageLoadHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-01.
 */
class ImageListViewModelTest {

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

    private lateinit var imageListViewModel: ImageListViewModel
    private var capturedPageLoadApprove: ((key: String, pageNumber: Int, dataSize: Int, isFirstPage: Boolean) -> Unit)? = null

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
            mockPageLoadHelper.onPageLoadApprove = capture(lambdaSlot)
        } answers {
            capturedPageLoadApprove = lambdaSlot.captured
        }

        imageListViewModel = ImageListViewModel(mockApplication, mockRepository, mockPageLoadHelper)
    }

    private fun initUtils() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns -1
    }

    @Test
    fun `한줄에_표현되는_아이템의_갯수를_변경하면_반영되는지_테스트`() {
        // when
        imageListViewModel.changeCountOfItemInLine(2)
        imageListViewModel.changeCountOfItemInLine(4)

        // then
        imageListViewModel.countOfItemInLine.observeForever { count ->
            assertEquals(4, count)
        }
    }

    @Test
    fun `최초_이미지_목록_요청시_수신한_이미지_목록이_반영되는지_테스트`() {
        // given
        val expect = createVirtualImageSearchResponse(3, true)
        every { mockRepository.requestImageList(any()) } returns (Single.just(expect))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageListViewModel.loadImageList("테스트")

        // then
        imageListViewModel.imageDocumentList.observeForever { actuallyList ->
            assertEquals(expect.imageDocumentList, actuallyList)
        }
    }

    @Test
    fun `최초 이미지 목록_요청시_수신한_리스트의_사이즈가_0이어도_반영되는지_테스트`() {
        // given
        val expect = createVirtualImageSearchResponse(0, true)
        every { mockRepository.requestImageList(any()) } returns (Single.just(expect))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageListViewModel.loadImageList("테스트")

        // then
        imageListViewModel.imageDocumentList.observeForever { actuallyList ->
            assertEquals(0, actuallyList.size)
        }
    }

    @Test
    fun `최초_이미지_목록_요청시_수신한_리스트의_사이즈가_0이라면_검색결과가_없다는_메시지가_반영되는지_테스트`() {
        // given
        val expect = createVirtualImageSearchResponse(0, true)
        every { mockRepository.requestImageList(any()) } returns (Single.just(expect))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageListViewModel.loadImageList("테스트")

        // then
        imageListViewModel.showMessageEvent.observeForever { message ->
            assertEquals(message, SUCCESS_NO_RESULT)
        }
    }

    @Test
    fun `최초_이미지_목록_요청시_네트워크 연결 에러가_수신된다면_에러_메시지가_반영되는지_테스트`() {
        // given
        every { mockRepository.requestImageList(any()) } returns (Single.error(RepositoryException.NetworkNotConnectingException("network error")))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageListViewModel.loadImageList("테스트")

        // then
        imageListViewModel.showMessageEvent.observeForever { message ->
            assertEquals(message, ERROR_NETWORK)
        }
    }

    @Test
    fun `마지막_데이터라면_마지막을_뜻하는_메시지가_반영되는지_테스트`() {
        // given
        every { mockRepository.requestImageList(any()) } returns (Single.just(createVirtualImageSearchResponse(1, true)))

        // when
        imageListViewModel.loadImageList("테스트");

        // then
        imageListViewModel.showMessageEvent.observeForever { message ->
            assertEquals(message, SUCCESS_LAST_DATA)
        }
    }

    @Test
    fun `추가_이미지_목록_요청시_수신된_추가_이미지_목록이_반영되는지_테스트`() {
        // given
        var isFirst = true
        val firstValue = createVirtualImageSearchResponse(3, false)
        val secondValue = createVirtualImageSearchResponse(3, true)
        val expectedList = mutableListOf<ImageDocument>().apply {
            addAll(firstValue.imageDocumentList)
            addAll(secondValue.imageDocumentList)
        }

        every { mockRepository.requestImageList(any()) } returns if(isFirst) {
            isFirst = false
            Single.just(firstValue)
        } else {
            Single.just(secondValue)
        }
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, true) }
        every { mockPageLoadHelper.requestPreloadIfPossible(any(), any(), any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }


        // when
        imageListViewModel.loadImageList("테스트")
        imageListViewModel.loadMoreImageListIfPossible(3)

        // then
        imageListViewModel.imageDocumentList.observeForever { actuallyList ->
            assertEquals(expectedList, actuallyList)
        }
    }

    @Test
    fun `추가_이미지_목록_요청시_마지막_데이터였다면_레파지토리에_요청을_안하는지_테스트`() {
        // given
        every { mockRepository.requestImageList(any()) } returns (Single.just(createVirtualImageSearchResponse(1, true)))
        every { mockPageLoadHelper.requestFirstLoad(any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, true) }
        every { mockPageLoadHelper.requestPreloadIfPossible(any(), any(), any()) } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageListViewModel.loadImageList("테스트")
        imageListViewModel.loadMoreImageListIfPossible(1)
        imageListViewModel.loadMoreImageListIfPossible(1)
        imageListViewModel.loadMoreImageListIfPossible(1)

        // then
        verify(exactly = 1) { mockRepository.requestImageList(any()) } // 최초 1번
    }

    @Test
    fun `재시도_요청시_레파지토리에_요청을_하는지_테스트`() {
        // given
        every { mockRepository.requestImageList(any()) } returns (Single.just(createVirtualImageSearchResponse(1, true)))
        every { mockPageLoadHelper.requestRetryAsPreviousValue() } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        imageListViewModel.retryLoadMoreImageList()

        //then
        verify(exactly = 1) { mockRepository.requestImageList(any()) }
    }

    @Test
    fun `이미지_검색_타입_변경시_반영되는지_테스트`() {
        // given
        every { mockRepository.requestImageList(any()) } returns (Single.just(createVirtualImageSearchResponse(1, true)))
        every { mockPageLoadHelper.requestRetryAsPreviousValue() } answers { capturedPageLoadApprove?.invoke("", 1, 1, false) }

        // when
        val typeList = mutableListOf<ImageSortType>()
        imageListViewModel.imageSortType.observeForever { type ->
            typeList.add(type)
        }
        imageListViewModel.changeImageSortType(ImageSortType.ACCURACY)
        imageListViewModel.changeImageSortType(ImageSortType.RECENCY)

        // then
        assertEquals(ImageSortType.ACCURACY, typeList[0]) // 최초 default 로 설정되어 있는 값
        assertEquals(ImageSortType.ACCURACY, typeList[1])
        assertEquals(ImageSortType.RECENCY, typeList[2])
    }

    @Test
    fun `이미지 클릭 시 이미지 상세 화면 이동 이벤트 호출되는지 테스트`() {
        // given
        val imageDocument = createVirtualImageDocument(0)

        // when
        imageListViewModel.onClickImage(imageDocument)

        // then
        imageListViewModel.moveToDetailScreenEvent.observeForever { receivedImageDocument ->
            assertEquals(imageDocument, receivedImageDocument)
        }
    }

    private fun createVirtualImageSearchResponse(
        documentListSize: Int,
        isLastData: Boolean
    ): ImageSearchResponse {
        val searchMeta = ImageSearchMeta(isLastData)
        val imageDocumentList = createVirtualImageDocumentList(documentListSize)
        return ImageSearchResponse(searchMeta, imageDocumentList)
    }

    private fun createVirtualImageDocumentList(size: Int): MutableList<ImageDocument> {
        val list = mutableListOf<ImageDocument>()
        for(i in 0 until size) {
            list.add(createVirtualImageDocument(i))
        }
        return list
    }

    private fun createVirtualImageDocument(id: Int): ImageDocument {
        return ImageDocument("thumbnailUrl$id", "imageUrlInfo$id", "docUrl$id")
    }
    
}