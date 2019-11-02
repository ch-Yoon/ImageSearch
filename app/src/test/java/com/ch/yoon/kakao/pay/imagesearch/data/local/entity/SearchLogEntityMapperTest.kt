package com.ch.yoon.kakao.pay.imagesearch.data.local.entity

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLogEntityMapper
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLogModel
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLog
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class SearchLogEntityMapperTest {

    @Test
    fun `SearchLogModel을 searchLog로 맵핑하는지 테스트`() {
        // given
        val searchLogModel1 = SearchLogModel("테스트1", 1)
        val expectedSearchLog1 = SearchLog("테스트1", 1)
        val searchLogModel2 = SearchLogModel("테스트2", 2)
        val expectedSearchLog2 = SearchLog("테스트2", 2)
        val searchLogModel3 = SearchLogModel("테스트3", 3)
        val expectedSearchLog3 = SearchLog("테스트3", 3)

        // when
        val searchLog1 = SearchLogEntityMapper.toEntity(searchLogModel1)
        val searchLog2 = SearchLogEntityMapper.toEntity(searchLogModel2)
        val searchLog3 = SearchLogEntityMapper.toEntity(searchLogModel3)

        // then
        assertEquals(expectedSearchLog1, searchLog1)
        assertEquals(expectedSearchLog2, searchLog2)
        assertEquals(expectedSearchLog3, searchLog3)
    }

    @Test
    fun `SearchLogModelList를 SearchLogList로 맵핑하는지 테스트`() {
        // given
        val searchLogModelList = mutableListOf<SearchLogModel>()
        searchLogModelList.add(SearchLogModel("테스트1", 1))
        searchLogModelList.add(SearchLogModel("테스트2", 2))
        searchLogModelList.add(SearchLogModel("테스트3", 3))

        val expectedSearchLogList = mutableListOf<SearchLog>()
        expectedSearchLogList.add(SearchLog("테스트1", 1))
        expectedSearchLogList.add(SearchLog("테스트2", 2))
        expectedSearchLogList.add(SearchLog("테스트3", 3))

        // when
        val actualSearchLogList = SearchLogEntityMapper.toEntityList(searchLogModelList)

        // then
        assertEquals(expectedSearchLogList, actualSearchLogList)
    }
}
