package com.ch.yoon.imagesearch.extension

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Creator : ch-yoon
 * Date : 2019-11-02
 **/
class LetExtensionsTest {

    @Test
    fun `파라미터가 2개가 모두 null이 아닐 때 safeLet이 호출되는지 test`() {
        // given
        val first = 10
        val second = 20

        // when
        var callCount = 0
        var actualFirstValue: Int? = null
        var actualSecondValue: Int? = null
        safeLet(first, second) { firstValue, secondValue ->
            callCount = 1
            actualFirstValue = firstValue
            actualSecondValue = secondValue
        }

        // then
        assertEquals(1, callCount)
        assertEquals(first, actualFirstValue)
        assertEquals(second, actualSecondValue)
    }

    @Test
    fun `파라미터가 1개가 null일때 safeLet이 호출되지 않는지 테스트`() {
        // when
        var callCount = 0
        safeLet(10, null) { _, _ -> callCount = 1 }

        // then
        assertEquals(0, callCount)

        // when
        safeLet(null, 10) { _, _ -> callCount = 1 }

        // then
        assertEquals(0, callCount)
    }

    @Test
    fun `파라미터가 3개가 모두 null이 아닐 때 safeLet이 호출되는지 test`() {
        // given
        val first = 10
        val second = 20
        val third = 30

        // when
        var callCount = 0
        var actualFirstValue: Int? = null
        var actualSecondValue: Int? = null
        var actualThirdValue: Int? = null
        safeLet(first, second, third) { firstValue, secondValue, thirdValue ->
            callCount = 1
            actualFirstValue = firstValue
            actualSecondValue = secondValue
            actualThirdValue = thirdValue
        }

        // then
        assertEquals(1, callCount)
        assertEquals(first, actualFirstValue)
        assertEquals(second, actualSecondValue)
        assertEquals(third, actualThirdValue)
    }

    @Test
    fun `파라미터 3개 중 1개가 null일때 safeLet 호출되지 않는지 테스트`() {
        // when
        var callCount = 0
        safeLet(null, 10, 10) { _, _, _ -> callCount = 1 }

        // then
        assertEquals(0, callCount)

        // when
        safeLet(10, null, 10) { _, _, _ -> callCount = 1 }

        // then
        assertEquals(0, callCount)

        // when
        safeLet(10, 10, null) { _, _, _ -> callCount = 1 }

        // then
        assertEquals(0, callCount)
    }

    @Test
    fun `파라미터 3개 중 2개가 null일때 safeLet 호출되지 않는지 테스트`() {
        // when
        var callCount = 0
        safeLet(null, null, 10) { _, _, _ -> callCount = 1 }

        // then
        assertEquals(0, callCount)

        // when
        safeLet(null, 10, null) { _, _, _ -> callCount = 1 }

        // then
        assertEquals(0, callCount)

        // when
        safeLet(10, null, null) { _, _, _ -> callCount = 1 }

        // then
        assertEquals(0, callCount)
    }
}