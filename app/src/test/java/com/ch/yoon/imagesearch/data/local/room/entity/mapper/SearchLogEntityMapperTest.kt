package com.ch.yoon.imagesearch.data.local.room.entity.mapper

import com.ch.yoon.local.entity.SearchLogEntity
import com.ch.yoon.data.model.searchlog.SearchLog
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class SearchLogEntityMapperTest {

    @Test
    fun `SearchLogEntity를 searchLog로 맵핑하는지 테스트`() {
        // given
        val searchLog1 = com.ch.yoon.local.entity.SearchLogEntity("테스트1", 1)
        val expectedSearchLogEntity1 = com.ch.yoon.data.model.searchlog.SearchLog("테스트1", 1)
        val searchLog2 = com.ch.yoon.local.entity.SearchLogEntity("테스트2", 2)
        val expectedSearchLogEntity2 = com.ch.yoon.data.model.searchlog.SearchLog("테스트2", 2)
        val searchLog3 = com.ch.yoon.local.entity.SearchLogEntity("테스트3", 3)
        val expectedSearchLogEntity3 = com.ch.yoon.data.model.searchlog.SearchLog("테스트3", 3)

        // when
        val searchLogEntity1 = com.ch.yoon.local.entity.mapper.SearchLogEntityMapper.fromEntity(searchLog1)
        val searchLogEntity2 = com.ch.yoon.local.entity.mapper.SearchLogEntityMapper.fromEntity(searchLog2)
        val searchLogEntity3 = com.ch.yoon.local.entity.mapper.SearchLogEntityMapper.fromEntity(searchLog3)

        // then
        assertEquals(expectedSearchLogEntity1, searchLogEntity1)
        assertEquals(expectedSearchLogEntity2, searchLogEntity2)
        assertEquals(expectedSearchLogEntity3, searchLogEntity3)
    }

    @Test
    fun `SearchLogEntityList를 SearchLogList로 맵핑하는지 테스트`() {
        // given
        val searchLogList = mutableListOf<com.ch.yoon.local.entity.SearchLogEntity>()
        searchLogList.add(com.ch.yoon.local.entity.SearchLogEntity("테스트1", 1))
        searchLogList.add(com.ch.yoon.local.entity.SearchLogEntity("테스트2", 2))
        searchLogList.add(com.ch.yoon.local.entity.SearchLogEntity("테스트3", 3))

        val expectedSearchLogEntityList = mutableListOf<com.ch.yoon.data.model.searchlog.SearchLog>()
        expectedSearchLogEntityList.add(com.ch.yoon.data.model.searchlog.SearchLog("테스트1", 1))
        expectedSearchLogEntityList.add(com.ch.yoon.data.model.searchlog.SearchLog("테스트2", 2))
        expectedSearchLogEntityList.add(com.ch.yoon.data.model.searchlog.SearchLog("테스트3", 3))

        // when
        val actualSearchLogList = com.ch.yoon.local.entity.mapper.SearchLogEntityMapper.fromEntityList(searchLogList)

        // then
        assertEquals(expectedSearchLogEntityList, actualSearchLogList)
    }
}
