package com.ch.yoon.imagesearch.presentation.search.searchbox

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogRepository
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    lateinit var mockSearchLogRepository: SearchLogRepository

    private lateinit var searchBoxViewModel: SearchBoxViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        initApplication()
        initUtils()
        initSearchBoxViewModel()
    }

    private fun initApplication() {
        every { mockApplication.getString(R.string.success_delete_all_search_log) } returns DELETE_SUCCESS_MESSAGE
        every { mockApplication.getString(R.string.empty_keyword_guide) } returns EMPTY_KEYWORD_MESSAGE
    }

    private fun initUtils() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns -1
    }

    private fun initSearchBoxViewModel() {
        searchBoxViewModel = SearchBoxViewModel(mockApplication, mockSearchLogRepository)
    }

    @Test
    fun `검색 기록 로드가 되는지 테스트`() {
        // given
        every {mockSearchLogRepository.getAllSearchLogs() } returns (Single.just(createVirtualSearchLogList(3)))

        // when
        searchBoxViewModel.loadSearchLogs()

        // then
        searchBoxViewModel.searchLogs.observeForever { receivedList ->
            assertEquals(3, receivedList.size)
        }
    }

    @Test
    fun `검색 기록 로드시 내림차순으로 정렬되는지 테스트`() {
        // given
        val searchLogList = createVirtualSearchLogList(3)
        every { mockSearchLogRepository.getAllSearchLogs() } returns (Single.just(searchLogList))

        // when
        searchBoxViewModel.loadSearchLogs()

        // then
        val expectedList = createVirtualSearchLogList(3).sorted()
        searchBoxViewModel.searchLogs.observeForever { receivedList ->
            assertEquals(expectedList, receivedList)
        }
    }

    @Test
    fun `키워드 검색 버튼 클릭시 기존에 검색했던 키워드라면 목록의 가장 앞쪽으로 이동시키는지 테스트`() {
        // given
        every { mockSearchLogRepository.getAllSearchLogs() } returns Single.just(createVirtualSearchLogList(3))
        every { mockSearchLogRepository.insertOrUpdateSearchLog("테스트0") } returns (Single.just(SearchLog("테스트0", 4)))

        // when
        searchBoxViewModel.loadSearchLogs()
        searchBoxViewModel.onClickSearchButton("테스트0")

        // then
        searchBoxViewModel.searchLogs.observeForever{ searchLogs ->
            assertEquals(SearchLog("테스트0", 4), searchLogs[0])
            assertEquals(3, searchLogs.size)
        }
    }

    @Test
    fun `키워드가 비어있다면 검색 거절 메시지가 반영되는지 테스트`() {
        // when
        searchBoxViewModel.onClickSearchButton("")

        // then
        searchBoxViewModel.showMessageEvent.observeForever { message ->
            assertEquals(EMPTY_KEYWORD_MESSAGE, message)
        }
    }

    @Test
    fun `검색 상자 여는 이벤트가 발생하는지 테스트`() {
        // when
        searchBoxViewModel.onStateChange(false)
        searchBoxViewModel.onClickShowButton()

        // then
        searchBoxViewModel.searchBoxEnableEvent.observeForever {
            assertEquals(true, it)
        }
    }

    @Test
    fun `검색상자가 열려있을 때 뒤로가기 클릭 시 검색상자 닫기 이벤트가 호출되는지 테스트`() {
        // when
        var closeEventCount = 0
        searchBoxViewModel.searchBoxEnableEvent.observeForever { closeEventCount++ }
        searchBoxViewModel.onStateChange(true)
        searchBoxViewModel.onClickHideButton()

        // then
        assertEquals(1, closeEventCount)
    }

    @Test
    fun `검색상자의 상태를 닫는 함수 호출 시 닫힌 상태가 되는지 테스트`() {
        // when
        searchBoxViewModel.onStateChange(false)

        // then
        assertEquals(false, searchBoxViewModel.isOpen)
    }

    @Test
    fun `검색상자의 상태를 여는 함수 호출 시 열린 상태가 되는지 테스트`() {
        // when
        searchBoxViewModel.onStateChange(true)

        // then
        assertEquals(true, searchBoxViewModel.isOpen)
    }

    @Test
    fun `키워드 삭제 버튼 클릭시 레파지토리에 삭제 요청을 하는지 테스트`() {
        // given
        val targetList = mutableListOf(SearchLog("테스트", 0))
        every { mockSearchLogRepository.getAllSearchLogs() } returns (Single.just(targetList))
        every { mockSearchLogRepository.deleteSearchLog(any()) } returns (Completable.complete())

        // when
        searchBoxViewModel.loadSearchLogs()
        searchBoxViewModel.onClickSearchLogDeleteButton(targetList[0])

        // then
        verify(exactly = 1) { mockSearchLogRepository.deleteSearchLog(targetList[0]) }
    }

    @Test
    fun `키워드 삭제 버튼 클릭시 보유하던 검색 기록 목록에서 제거하는지 테스트`() {
        // given
        val searchLogList = createVirtualSearchLogList(3)
        val expectedList = createVirtualSearchLogList(3).apply { removeAt(0) }.sorted()

        every { mockSearchLogRepository.getAllSearchLogs() } returns (Single.just(searchLogList))
        every { mockSearchLogRepository.deleteSearchLog(any()) } returns(Completable.complete())

        // when
        searchBoxViewModel.loadSearchLogs()
        searchBoxViewModel.onClickSearchLogDeleteButton(searchLogList[0])

        // then
        searchBoxViewModel.searchLogs.observeForever{ receivedList ->
            assertEquals(expectedList, receivedList)
        }
    }

    @Test
    fun `키워드 전체 삭제 버튼 클릭시 보유하던 검색 기록 목록이 전부 제거되는지 테스트`() {
        // given
        val searchLogList = createVirtualSearchLogList(3)

        every { mockSearchLogRepository.getAllSearchLogs() } returns (Single.just(searchLogList))
        every { mockSearchLogRepository.deleteAllSearchLogs() } returns(Completable.complete())

        // when
        searchBoxViewModel.loadSearchLogs()
        searchBoxViewModel.onClickSearchLogAllDelete()

        // then
        searchBoxViewModel.searchLogs.observeForever{ receivedList ->
            assertEquals(0, receivedList.size)
        }
    }

    private fun createVirtualSearchLogList(size: Int): MutableList<SearchLog> {
        val searchLogList = mutableListOf<SearchLog>()
        for (i in 0 until size) {
            searchLogList.add(SearchLog("테스트$i", i.toLong()))
        }

        return searchLogList
    }
}