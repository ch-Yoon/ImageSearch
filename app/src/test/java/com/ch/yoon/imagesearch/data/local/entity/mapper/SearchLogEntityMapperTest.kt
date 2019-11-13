package com.ch.yoon.imagesearch.data.local.entity.mapper

import com.ch.yoon.imagesearch.data.local.room.entity.mapper.SearchLogEntityMapper
import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntity
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class SearchLogEntityMapperTest {

    @Test
    fun `SearchLog를 searchLogEntity로 맵핑하는지 테스트`() {
        // given
        val searchLog1 = SearchLogEntity("테스트1", 1)
        val expectedSearchLogEntity1 = SearchLog("테스트1", 1)
        val searchLog2 = SearchLogEntity("테스트2", 2)
        val expectedSearchLogEntity2 = SearchLog("테스트2", 2)
        val searchLog3 = SearchLogEntity("테스트3", 3)
        val expectedSearchLogEntity3 = SearchLog("테스트3", 3)

        // when
        val searchLogEntity1 = SearchLogEntityMapper.fromEntity(searchLog1)
        val searchLogEntity2 = SearchLogEntityMapper.fromEntity(searchLog2)
        val searchLogEntity3 = SearchLogEntityMapper.fromEntity(searchLog3)

        // then
        assertEquals(expectedSearchLogEntity1, searchLogEntity1)
        assertEquals(expectedSearchLogEntity2, searchLogEntity2)
        assertEquals(expectedSearchLogEntity3, searchLogEntity3)
    }

    @Test
    fun `SearchLogList를 SearchLogList로 맵핑하는지 테스트`() {
        // given
        val searchLogList = mutableListOf<SearchLogEntity>()
        searchLogList.add(SearchLogEntity("테스트1", 1))
        searchLogList.add(SearchLogEntity("테스트2", 2))
        searchLogList.add(SearchLogEntity("테스트3", 3))

        val expectedSearchLogEntityList = mutableListOf<SearchLog>()
        expectedSearchLogEntityList.add(SearchLog("테스트1", 1))
        expectedSearchLogEntityList.add(SearchLog("테스트2", 2))
        expectedSearchLogEntityList.add(SearchLog("테스트3", 3))

        // when
        val actualSearchLogList = SearchLogEntityMapper.fromEntityList(searchLogList)

        // then
        assertEquals(expectedSearchLogEntityList, actualSearchLogList)
    }
}
