package com.ch.yoon.imagesearch.data.local

import com.belongings.bag.belongingsbag.RxSchedulerRule
import com.ch.yoon.imagesearch.data.local.room.SearchLogLocalDataSourceImpl
import com.ch.yoon.imagesearch.data.local.room.dao.SearchLogDAO
import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntity
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogLocalDataSource
import com.ch.yoon.imagesearch.data.repository.error.RepositoryException
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
class SearchLogLocalDataSourceImplTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    lateinit var mockSearchLogDao: SearchLogDAO

    private lateinit var searchLogLocalDataSource: SearchLogLocalDataSource

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        searchLogLocalDataSource = SearchLogLocalDataSourceImpl(mockSearchLogDao)
    }

    @Test
    fun `키워드 업데이트 요청이 들어왔을 때 DAO로 업데이트 요청을 전달하는지 테스트`() {
        // given
        every { mockSearchLogDao.insertOrUpdateSearchLog(any()) } returns Completable.complete()

        // when
        searchLogLocalDataSource.insertOrUpdateSearchLog("테스트")

        // then
        verify(exactly = 1) { mockSearchLogDao.insertOrUpdateSearchLog(any()) }
    }

    @Test
    fun `키워드 업데이트 요청 시 searchLog를 반환하는지 테스트`() {
        // given
        every { mockSearchLogDao.insertOrUpdateSearchLog(any()) } returns Completable.complete()

        // when
        var searchLog: SearchLog? = null
        searchLogLocalDataSource.insertOrUpdateSearchLog("테스트")
            .subscribe({ receivedSearchLog ->
                searchLog = receivedSearchLog
            }, { throwable ->
                searchLog = null
            })

        // then
        assertEquals("테스트", searchLog?.keyword ?: "")
    }

    @Test
    fun `키워드 업데이트 요청 시 에러 발생되면 Repository Exception으로 반환하는지 테스트`() {
        // given
        every { mockSearchLogDao.insertOrUpdateSearchLog(any()) } returns Completable.error(Exception())

        // when
        var exception: RepositoryException? = null
        searchLogLocalDataSource.insertOrUpdateSearchLog("테스트")
            .subscribe({ receivedSearchLog ->
                exception = null
            }, { throwable ->
                if(throwable is RepositoryException) {
                    exception = throwable
                } else {
                    exception = null
                }
            })

        assertEquals(true, exception is RepositoryException)
    }

    @Test
    fun `검색 요청 시 비어있는 searchLogList를 반환하는지 테스트`() {
        // given
        every { mockSearchLogDao.selectAllSearchLog() } returns Single.just(emptyList())

        // when
        var list: List<SearchLog>? = null
        searchLogLocalDataSource.selectAllSearchLog()
            .subscribe({ receivedList ->
                list = receivedList
            }, { throwable ->
                list = null
            })

        // then
        assertEquals(0, list?.size)
    }

    @Test
    fun `검색 요청 시 searchLogList를 반환하는지 테스트`() {
        // given
        val searchLogModelList = createSearchLogModelList(3)
        every { mockSearchLogDao.selectAllSearchLog() } returns Single.just(searchLogModelList)

        // when
        var actualList: List<SearchLog>? = null
        searchLogLocalDataSource.selectAllSearchLog()
            .subscribe({ list ->
                actualList = list
            }, { throwable ->
                actualList = null
            })

        // then
        val expectedList = searchLogModelList.map { SearchLog(it.keyword, it.time) }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `검색 기록 삭제 요청 시 삭제 요청을 DAO로 전달하는지 테스트`() {
        // given
        every { mockSearchLogDao.deleteSearchLog(any(), any()) } returns Completable.complete()

        // when
        val searchLog = SearchLog("테스트", 1)
        searchLogLocalDataSource.deleteSearchLog(searchLog)

        // then
        verify(exactly = 1) { mockSearchLogDao.deleteSearchLog("테스트", 1) }
    }

    @Test
    fun `검색 기록 삭제 요청 시 에러가 발생했다면 RepositoryException 발생하는지 테스트`() {
        // given
        every { mockSearchLogDao.deleteSearchLog(any(), any()) } returns Completable.error(Exception())

        // when
        var actualException: RepositoryException? = null
        val searchLog = SearchLog("테스트", 1)
        searchLogLocalDataSource.deleteSearchLog(searchLog)
            .subscribe({
                actualException = null
            }) { throwable ->
                actualException = when (throwable) {
                    is RepositoryException -> {
                        throwable
                    }
                    else -> {
                        null
                    }
                }
            }

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