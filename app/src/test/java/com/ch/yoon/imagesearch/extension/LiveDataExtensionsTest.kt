package com.ch.yoon.imagesearch.extension

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class LiveDataExtensionsTest {

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `첫번째 대상을 삭제한 뒤 가장 앞쪽에 삽입되는지 테스트`() {
        // given
        val liveData = MutableLiveData<MutableList<Int>>(mutableListOf(1, 2, 2, 3, 4))

        // when
        liveData.removeFirstAndAddFirst(5) { it == 2 }

        // then
        val expected = mutableListOf(5, 1, 2, 3, 4)
        assertEquals(expected, liveData.value)
    }

    @Test
    fun `첫번째 대상을 삭제하는지 테스트`() {
        // given
        val liveData = MutableLiveData<MutableList<Int>>(mutableListOf(1, 2, 2, 3, 4))

        // when
        liveData.removeFirst { it == 2 }

        // then
        val expected = mutableListOf(1, 2, 3, 4)
        assertEquals(expected, liveData.value)
    }

    @Test
    fun `조건에 해당하는 대상을 찾는지 테스트`() {
        // given
        val liveData = MutableLiveData<MutableList<Int>>(mutableListOf(1, 2, 2, 3, 4))

        // when
        val target = liveData.find { it == 3 }

        // then
        val expected = 3
        assertEquals(expected, target)
    }

    @Test
    fun `리스트를 클리어하는지 테스트`() {
        // given
        val liveData = MutableLiveData<MutableList<Int>>(mutableListOf(1, 2, 2, 3, 4))

        // when
        liveData.clear()

        // then
        assertEquals(0, liveData.value?.size)
    }

    @Test
    fun `empty 테스트`() {
        // given
        val nonEmptyLiveData = MutableLiveData<MutableList<Int>>(mutableListOf(1, 2, 2, 3, 4))
        val emptyLiveData = MutableLiveData<MutableList<Int>>()

        // when && then
        assertEquals(false, nonEmptyLiveData.isEmpty())
        assertEquals(true, emptyLiveData.isEmpty())
    }

    @Test
    fun `notEmpty 테스트`() {
        // given
        val nonEmptyLiveData = MutableLiveData<MutableList<Int>>(mutableListOf(1, 2, 2, 3, 4))
        val emptyLiveData = MutableLiveData<MutableList<Int>>()

        // when && then
        assertEquals(true, nonEmptyLiveData.isNotEmpty())
        assertEquals(false, emptyLiveData.isNotEmpty())
    }

    @Test
    fun `addAll 테스트`() {
        // given
        val nonEmptyLiveData = MutableLiveData<MutableList<Int>>(mutableListOf(1, 2, 2, 3, 4))
        val emptyLiveData = MutableLiveData<MutableList<Int>>()

        // when
        nonEmptyLiveData.addAll(mutableListOf(1, 2, 3))
        emptyLiveData.addAll(mutableListOf(1, 2, 3))

        // then
        assertEquals(mutableListOf(1, 2, 2, 3, 4, 1, 2, 3), nonEmptyLiveData.value)
        assertEquals(mutableListOf(1, 2, 3), emptyLiveData.value)
    }
}