package com.ch.yoon.imagesearch.data.repository.searchlog

import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.BaseRxTest
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-12
 **/
class SearchLogRepositoryImplTest : BaseRxTest() {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    private lateinit var mockSearchLogLocalDataSource: SearchLogLocalDataSource

    private lateinit var imageRepository: SearchLogRepository

    override fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        imageRepository = SearchLogRepositoryImpl(mockSearchLogLocalDataSource)
    }

    override fun after() {
    }

    @Test
    fun `업데이트 요청 성공 테스트`() {
        // given
        val updatedLog = SearchLog("테스트", 1)
        every {
            mockSearchLogLocalDataSource.insertOrUpdateSearchLog(any())
        } returns Single.just(updatedLog)

        // when
        var actualLog: SearchLog? = null
        imageRepository.insertOrUpdateSearchLog("테스트")
            .subscribe({ actualLog = it }, {})
            .register()

        // then
        assertEquals(updatedLog, actualLog)
    }

    @Test
    fun `업데이트 실패 시 RepositoryException 반환 테스트`() {
        // given
        every {
            mockSearchLogLocalDataSource.insertOrUpdateSearchLog(any())
        } returns Single.error(RepositoryException.UnknownException(""))

        // when
        var actualException: RepositoryException? = null
        imageRepository.insertOrUpdateSearchLog("테스트")
            .subscribe({}, { actualException = if(it is RepositoryException) it else null })
            .register()

        // then
        assertEquals(true, actualException is RepositoryException)
    }

    @Test
    fun `검색 목록을 반환하는지 테스트`() {
        // given
        val publishSearchLogList = createSearchLogList(3)
        every {
            mockSearchLogLocalDataSource.getAllSearchLogs()
        } returns Single.just(publishSearchLogList)

        // when
        var actualList: List<SearchLog>? = null
        imageRepository.getAllSearchLogs()
            .subscribe({ actualList = it }, {})
            .register()

        // then
        assertEquals(publishSearchLogList, actualList)
    }

    @Test
    fun `비어있는 검색 목록 반환하는지 테스트`() {
        // given
        every {
            mockSearchLogLocalDataSource.getAllSearchLogs()
        } returns Single.just(emptyList())

        // when
        var actualList: List<SearchLog>? = null
        imageRepository.getAllSearchLogs()
            .subscribe({ actualList = it }, {})
            .register()

        // then
        assertEquals(0, actualList?.size ?: -1)
    }

    @Test
    fun `검색 목록 에러 발생시 RepositoryException 반환 테스트`() {
        // given
        every {
            mockSearchLogLocalDataSource.getAllSearchLogs()
        } returns Single.error(RepositoryException.UnknownException(""))

        // when
        var actualException: RepositoryException? = null
        imageRepository.getAllSearchLogs()
            .subscribe({}, { actualException = if(it is RepositoryException) it else null })
            .register()

        // then
        assertEquals(true, actualException is RepositoryException)
    }

    @Test
    fun `키워드 삭제 성공 테스트`() {
        // given
        every {
            mockSearchLogLocalDataSource.deleteSearchLog(any())
        } returns Completable.complete()

        // when
        var isSuccess = false
        imageRepository.deleteSearchLog(emptySearchLog())
            .subscribe({ isSuccess = true }, {})

        // then
        assertEquals(true, isSuccess)
    }

    @Test
    fun `키워드 삭제 에러 발생시 RepositoryException 반환 테스트`() {
        // given
        every {
            mockSearchLogLocalDataSource.deleteSearchLog(any())
        } returns Completable.error(RepositoryException.UnknownException(""))

        // when
        var actualException: RepositoryException? = null
        imageRepository.deleteSearchLog(SearchLog("테스트", 0))
            .subscribe({}, { actualException = if(it is RepositoryException) it else null })
            .register()

        // then
        assertEquals(true, actualException is RepositoryException)
    }

    @Test
    fun `검색 기록 전체 삭제 요청 성공 테스트`() {
        // given
        every {
            mockSearchLogLocalDataSource.deleteAllSearchLogs()
        } returns Completable.complete()

        // when
        var isSuccess = false
        imageRepository.deleteAllSearchLogs()
            .subscribe({ isSuccess = true }, {})
            .register()

        // then
        assertEquals(true, isSuccess)
    }

    @Test
    fun `검색 기록 전체 삭제 실패 시 RepositoryException 반환 테스트`() {
        // given
        every {
            mockSearchLogLocalDataSource.deleteAllSearchLogs()
        } returns Completable.error(RepositoryException.UnknownException(""))

        // when
        var actualException: RepositoryException? = null
        imageRepository.deleteAllSearchLogs()
            .subscribe({}, { actualException = if(it is RepositoryException) it else null })
            .register()

        // then
        assertEquals(true, actualException is RepositoryException)
    }

    private fun emptySearchLog(): SearchLog {
        return SearchLog("", 0)
    }

    private fun createSearchLogList(size: Int): List<SearchLog> {
        val list = mutableListOf<SearchLog>()
        for(i in 0 until size) {
            list.add(SearchLog(i.toString(), i.toLong()))
        }
        return list
    }
}