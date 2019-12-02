package com.ch.yoon.imagesearch.data.local.room

import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.BaseRxTest
import com.ch.yoon.imagesearch.data.local.room.dao.SearchLogDAO
import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntity
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogLocalDataSource
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
 * Date : 2019-11-02
 **/
class SearchLogLocalDataSourceImplTest : BaseRxTest() {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    lateinit var mockSearchLogDao: SearchLogDAO

    private lateinit var searchLogLocalDataSource: SearchLogLocalDataSource

    override fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        searchLogLocalDataSource = SearchLogLocalDataSourceImpl(mockSearchLogDao)
    }

    override fun after() {
    }

    @Test
    fun `키워드 업데이트가 성공하는지 테스트`() {
        // given
        every { mockSearchLogDao.insertOrUpdateSearchLog(any()) } returns Completable.complete()

        // when
        var isSuccess = false
        searchLogLocalDataSource.insertOrUpdateSearchLog("테스트")
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
    fun `키워드 업데이트 에러 발생시 Repository Exception 으로 반환하는지 테스트`() {
        // given
        every {
            mockSearchLogDao.insertOrUpdateSearchLog(any())
        } returns Completable.error(Exception())

        // when
        var exception: RepositoryException? = null
        searchLogLocalDataSource.insertOrUpdateSearchLog("테스트")
            .subscribe({
                exception = null
            }, {
                exception = if(it is RepositoryException) it else null
            })
            .register()

        assertEquals(true, exception is RepositoryException)
    }

    @Test
    fun `키워드 업데이트시 업데이트 된 searchLog를 반환하는지 테스트`() {
        // given
        every {
            mockSearchLogDao.insertOrUpdateSearchLog(any())
        } returns Completable.complete()

        // when
        var receivedSearchLog: SearchLog? = null
        searchLogLocalDataSource.insertOrUpdateSearchLog("테스트")
            .subscribe({
                receivedSearchLog = it
            }, {
                receivedSearchLog = null
            })
            .register()

        // then
        assertEquals("테스트", receivedSearchLog?.keyword ?: "")
    }

    @Test
    fun `비어있는 검색 기록을 반환하는지 테스트`() {
        // given
        every { mockSearchLogDao.selectAllSearchLogs() } returns Single.just(emptyList())

        // when
        var receivedList: List<SearchLog>? = null
        searchLogLocalDataSource.getAllSearchLogs()
            .subscribe({
                receivedList = it
            }, {
                receivedList = null
            })

        // then
        assertEquals(0, receivedList?.size)
    }

    @Test
    fun `저장된 검색 기록을 반환하는지 테스트`() {
        // given
        val searchLogModelList = createSearchLogModelList(3)
        every { mockSearchLogDao.selectAllSearchLogs() } returns Single.just(searchLogModelList)

        // when
        var actualList: List<SearchLog>? = null
        searchLogLocalDataSource.getAllSearchLogs()
            .subscribe({
                actualList = it
            }, {
                actualList = null
            })

        // then
        val expectedList = searchLogModelList.map { SearchLog(it.keyword, it.time) }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `검색 기록 삭제가 성공하는지 테스트`() {
        // given
        every { mockSearchLogDao.deleteSearchLog(any(), any()) } returns Completable.complete()

        // when
        var isSuccess = false
        searchLogLocalDataSource.deleteSearchLog(SearchLog("테스트", 1))
            .subscribe({
                isSuccess = true
            }, {
                isSuccess = false
            })

        // then
        assertEquals(true, isSuccess)
    }

    @Test
    fun `검색 기록 삭제 에러 발생 시 RepositoryException 발생하는지 테스트`() {
        // given
        every {
            mockSearchLogDao.deleteSearchLog(any(), any())
        } returns Completable.error(Exception())

        // when
        var actualException: RepositoryException? = null
        val searchLog = SearchLog("테스트", 1)
        searchLogLocalDataSource.deleteSearchLog(searchLog)
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
    fun `검색 기록 전체 삭제가 성공하는지 테스트`() {
        // given
        every { mockSearchLogDao.deleteAllSearchLog() } returns Completable.complete()

        // when
        var isSuccess = false
        searchLogLocalDataSource.deleteAllSearchLogs()
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
    fun `검색 기록 전체 삭제 에러 발생 시 RepositoryException 발생하는지 테스트`() {
        // given
        every { mockSearchLogDao.deleteAllSearchLog() } returns Completable.error(Exception())

        // when
        var actualException: RepositoryException? = null
        searchLogLocalDataSource.deleteAllSearchLogs()
            .subscribe({
                actualException = null
            }, {
                actualException = if(it is RepositoryException) it else null
            })
            .register()

        // then
        assertEquals(true, actualException is RepositoryException)
    }

    private fun createSearchLogModelList(size: Int): List<SearchLogEntity> {
        val list = mutableListOf<SearchLogEntity>()
        for(i in 0 until size) {
            list.add(SearchLogEntity("테스트$i", i.toLong()))
        }
        return list
    }
}