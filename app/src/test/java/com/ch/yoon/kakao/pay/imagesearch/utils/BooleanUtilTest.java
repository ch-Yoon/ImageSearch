package com.ch.yoon.kakao.pay.imagesearch.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Creator : ch-yoon
 * Date : 2019-08-13.
 */
public class BooleanUtilTest {

    @Test
    public void 값이_null일때_false로_판별하는지_테스트() {
        // when
        boolean isTrue = BooleanUtil.isTrue(null);
        boolean isFalse = BooleanUtil.isFalse(null);

        // then
        assertEquals(isTrue, false);
        assertEquals(isFalse, true);
    }

    @Test
    public void 값이_false일때_false로_판별하는지_테스트() {
        // when
        boolean isTrue = BooleanUtil.isTrue(false);
        boolean isFalse = BooleanUtil.isFalse(false);

        // then
        assertEquals(isTrue, false);
        assertEquals(isFalse, true);
    }

    @Test
    public void 값이_true일때_true로_판별하는지_테스트() {
        // when
        boolean isTrue = BooleanUtil.isTrue(true);
        boolean isFalse = BooleanUtil.isFalse(true);

        // then
        assertEquals(isTrue, true);
        assertEquals(isFalse, false);
    }
}
