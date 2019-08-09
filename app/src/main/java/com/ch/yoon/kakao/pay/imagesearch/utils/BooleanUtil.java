package com.ch.yoon.kakao.pay.imagesearch.utils;

import androidx.annotation.Nullable;

/**
 * Creator : ch-yoon
 * Date : 2019-08-09.
 */
public class BooleanUtil {

    public static boolean isTrue(@Nullable Boolean value) {
        return value != null && value;
    }

    public static boolean isFalse(@Nullable Boolean value) {
        return !isTrue(value);
    }

}
