package com.ch.yoon.kakao.pay.imagesearch.util.extension

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class CollectionExtensionsTest {

    @Test
    fun `조건에 해당하는 1개의 값만을 삭제하는지 revmoeFirstIf Test`() {
        // given
        val list = mutableListOf(1, 1, 2, 3, 4, 5, 6, 7)
        val expectedList = mutableListOf(1, 2, 3, 4, 5, 6, 7)

        // when
        list.removeFirstIf { before -> before == 1 }

        // then
        assertEquals(expectedList, list)
    }

    @Test
    fun `list의 가장 앞에 추가되는지 테스트`() {
        // given
        val list = mutableListOf(1, 1, 2, 3, 4, 5, 6, 7)
        val expectedList = mutableListOf(99, 1, 1, 2, 3, 4, 5, 6, 7)

        // when
        list.addFirst(99)

        // then
        assertEquals(expectedList, list)
    }

}