package com.ch.yoon.kakao.pay.imagesearch.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Creator : ch-yoon
 * Date : 2019-08-13.
 */
public class IntegerUtilTest {

    @Test
    public void 두값이_모두_null일때_같지_않다고_판별하는지_테스트() {
        // when && then
        assertEquals(IntegerUtil.isSame(null, null), false);
    }

    @Test
    public void 한개만_null일때_같지_않다고_판별하는지_테스트() {
        // when && then
        assertEquals(IntegerUtil.isSame(1, null), false);
        assertEquals(IntegerUtil.isSame(2, null), false);
        assertEquals(IntegerUtil.isSame(null, 1), false);
        assertEquals(IntegerUtil.isSame(null, 2), false);
    }

    @Test
    public void 두값이_서로_다를때_같지_않다고_판별하는지_테스트() {
        // when && then
        assertEquals(IntegerUtil.isSame(1, 2), false);
        assertEquals(IntegerUtil.isSame(2, 1), false);
        assertEquals(IntegerUtil.isSame(3, -1), false);
        assertEquals(IntegerUtil.isSame(4, 0), false);
    }

    @Test
    public void 두값이_서로_같을때_같다고_판별하는지_테스트() {
        // when && then
        assertEquals(IntegerUtil.isSame(1, 1), true);
        assertEquals(IntegerUtil.isSame(-1, -1), true);
        assertEquals(IntegerUtil.isSame(0, 0), true);
        assertEquals(IntegerUtil.isSame(15, 15), true);
    }

}
