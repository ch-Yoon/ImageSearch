package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLog
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import net.bytebuddy.matcher.ElementMatchers.returns
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * Creator : ch-yoon
 * Date : 2019-11-01.
 */
class SearchBoxViewModelTest {

    companion object {
        private const val DELETE_SUCCESS_MESSAGE = "삭제 완료"
        private const val EMPTY_KEYWORD_MESSAGE = "검색어를 입력해주세요"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    lateinit var mockApplication: Application
    @MockK
    lateinit var mockImageSearchRepository: ImageRepository

    private lateinit var searchBoxViewModel: SearchBoxViewModel


    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        initApplication()
        initUtils()
        initSearchBoxViewModel()
    }

    private fun initApplication() {
        every { mockApplication.getString(R.string.success_all_image_info_delete_by_keyword) } returns DELETE_SUCCESS_MESSAGE
        every { mockApplication.getString(R.string.empty_keyword_guide) } returns EMPTY_KEYWORD_MESSAGE
    }

    private fun initUtils() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns -1
    }

    private fun initSearchBoxViewModel() {
        searchBoxViewModel = SearchBoxViewModel(mockApplication, mockImageSearchRepository)
    }

    @Test
    fun `이미지 로드가 되는지 테스트`() {
        // given
        every {mockImageSearchRepository.requestSearchLogList() } returns (Single.just(createVirtualSearchLogList(3)));

        // when
        searchBoxViewModel.loadSearchLogList()

        // then
        searchBoxViewModel.searchLogList.observeForever { receivedList ->
            assertEquals(3, receivedList.size)
        }
    }

    @Test
    fun `키워드 검색 버튼 클릭시 입력한 키워드가 반영되는지 테스트`() {
        // given
        every { mockImageSearchRepository.insertOrUpdateSearchLog(any()) } returns (Single.just(SearchLog("테스트", 1)))

        // when
        searchBoxViewModel.onClickSearchButton("테스트")

        // then
        searchBoxViewModel.searchKeyword.observeForever { keyword ->
            assertEquals("테스트", keyword)
        }
    }

    @Test
    fun `키워드 검색 버튼 클릭시 키워드가 비어있다면 거절 메시지가 반영되는지 테스트`() {
        // when
        searchBoxViewModel.onClickSearchButton("")

        // then
        searchBoxViewModel.showMessageEvent.observeForever { message ->
            assertEquals(EMPTY_KEYWORD_MESSAGE, message)
        }
    }

    @Test
    fun `이미지 로드시 내림차순으로 정렬되는지 테스트`() {
        // given
        val searchLogList = createVirtualSearchLogList(3)
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(searchLogList))

        // when
        searchBoxViewModel.loadSearchLogList()

        // then
        val expectedList = createVirtualSearchLogList(3).sorted()
        searchBoxViewModel.searchLogList.observeForever { receivedList ->
            assertEquals(expectedList, receivedList)
        }
    }

    @Test
    fun `검색상자 클릭시 포커스를 갖는지 테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(mutableListOf()))

        // when
        searchBoxViewModel.onClickSearchBox()

        //then
        searchBoxViewModel.searchBoxFocus.observeForever { focus ->
            assertEquals(true, focus)
        }
    }

    @Test
    fun `키워드 검색 버튼 클릭시 키워드가 비어있지 않다면 포커스를 잃는지 테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(mutableListOf()))
        every { mockImageSearchRepository.insertOrUpdateSearchLog(any()) } returns (Single.just(SearchLog("테스트", 1)))

        // when
        searchBoxViewModel.loadSearchLogList()
        searchBoxViewModel.onClickSearchBox()
        searchBoxViewModel.onClickSearchButton("테스트")

        // then
        searchBoxViewModel.searchBoxFocus.observeForever { focus ->
            assertEquals(false, focus)
        }
    }

    @Test
    fun `키워드 검색 버튼 클릭시 키워드가 비어있다면 포커스를 계속 유지하는지 테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(mutableListOf()))

        // when
        searchBoxViewModel.loadSearchLogList()
        searchBoxViewModel.onClickSearchBox()
        searchBoxViewModel.onClickSearchButton("")

        //then
        searchBoxViewModel.searchBoxFocus.observeForever { focus ->
            assertEquals(true, focus)
        }
    }

    @Test
    fun `리스트가 비어있어도 포커스를 갖는지 테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(mutableListOf()))

        // when
        searchBoxViewModel.loadSearchLogList()
        searchBoxViewModel.onClickSearchBox()

        // then
        searchBoxViewModel.searchBoxFocus.observeForever { focus ->
            assertEquals(true, focus)
        }
    }

    @Test
    fun `배경 클릭시 갖고 있던 포커스를 잃는지 테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(mutableListOf()))

        // when
        searchBoxViewModel.onClickSearchBox()
        searchBoxViewModel.onClickBackground()

        //then
        searchBoxViewModel.searchBoxFocus.observeForever { focus ->
            assertEquals(false, focus)
        }
    }

    @Test
    fun `뒤로가기 클릭시 갖고있던 포커스를 잃는지 테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(mutableListOf()))

        // when
        searchBoxViewModel.onClickSearchBox()
        searchBoxViewModel.onClickBackPressButton()

        // then
        searchBoxViewModel.searchBoxFocus.observeForever { focus ->
            assertEquals(false, focus)
        }
    }

    @Test
    fun `뒤로가기 클릭시 포커스가 없다면 종료 이벤트가 호출되는지 테스트`() {
        // when
        var finishEventCount = 0
        searchBoxViewModel.searchBoxFinishEvent.observeForever { finishEventCount++ }
        searchBoxViewModel.onClickBackPressButton()

        // then
        assertEquals(1, finishEventCount)
    }

    @Test
    fun `뒤로가기 클릭시 포커스가 있다면 종료 이벤트가 호출되는지 않는지 테스트`() {
        // when
        var finishEventCount = 0
        searchBoxViewModel.searchBoxFinishEvent.observeForever { finishEventCount++ }
        searchBoxViewModel.onClickSearchBox()
        searchBoxViewModel.onClickBackPressButton()

        // then
        assertEquals(0, finishEventCount)
    }

    @Test
    fun `뒤로가기 두번 클릭시 포커스가 존재했다면 종료 이벤트가 호출되는지 테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(mutableListOf()))

        // when
        var finishEventCount = 0
        searchBoxViewModel.searchBoxFinishEvent.observeForever { finishEventCount++ }
        searchBoxViewModel.onClickSearchBox()
        searchBoxViewModel.onClickBackPressButton()
        searchBoxViewModel.onClickBackPressButton()

        // then
        assertEquals(1, finishEventCount)
    }

    @Test
    fun `키워드 삭제 버튼 클릭시 레파지토리에 삭제 요청을 하는지 테스트`() {
        // given
        every { mockImageSearchRepository.deleteSearchLog(any()) } returns (Completable.complete())

        // when
        val target = SearchLog("테스트", 1)
        searchBoxViewModel.onClickSearchLogDeleteButton(target)

        // then
        verify(exactly = 1) { mockImageSearchRepository.deleteSearchLog(target) }
    }

    @Test
    fun `키워드 삭제 버튼 클릭시 보유하던 검색 기록_목록에서 제거하는지 테스트`() {
        // given
        val searchLogList = createVirtualSearchLogList(3)
        val expectedList = createVirtualSearchLogList(3).apply { removeAt(0) }.sorted()

        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(searchLogList))
        every { mockImageSearchRepository.deleteSearchLog(any()) } returns(Completable.complete())

        // when
        searchBoxViewModel.loadSearchLogList()
        searchBoxViewModel.onClickSearchLogDeleteButton(searchLogList[0])

        // then
        searchBoxViewModel.searchLogList.observeForever{ receivedList ->
            assertEquals(expectedList, receivedList);
        }
    }

    @Test
    fun `키워드_검색_버튼_클릭시_기존에_검색했던_키워드라면_목록의_가장_앞쪽으로_이동시키는지_테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(createVirtualSearchLogList(3)))
        every { mockImageSearchRepository.insertOrUpdateSearchLog("테스트0") } returns (Single.just(SearchLog("테스트0", 4)))

        // when
        searchBoxViewModel.loadSearchLogList()
        searchBoxViewModel.onClickSearchButton("테스트0")

        // then
        searchBoxViewModel.searchLogList.observeForever{ searchLogs ->
            assertEquals("테스트0", searchLogs[0].keyword)
            assertEquals(3, searchLogs.size)
        }
    }

    @Test
    fun `실시간으로 키워드 변경 후 검색 버튼 클릭 시 검색 로그 저장 요청이 이루어지는지 테스트`() {
        // given
        every { mockImageSearchRepository.requestSearchLogList() } returns (Single.just(createVirtualSearchLogList(0)))
        every { mockImageSearchRepository.insertOrUpdateSearchLog("가나다") } returns (Single.just(SearchLog("가나다", 1)))

        // when
        searchBoxViewModel.onChangeKeyword("가")
        searchBoxViewModel.onChangeKeyword("가나")
        searchBoxViewModel.onChangeKeyword("가나다")
        searchBoxViewModel.onClickSearchButton()

        // then
        verify(exactly = 1) { mockImageSearchRepository.insertOrUpdateSearchLog("가나다") }
    }

    private fun createVirtualSearchLogList(size: Int): MutableList<SearchLog>  {
        val searchLogList = mutableListOf<SearchLog>()
        for(i in 0 until size) {
            searchLogList.add(SearchLog("테스트$i", i.toLong()))
        }

        return searchLogList
    }


}