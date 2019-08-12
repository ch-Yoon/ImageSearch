package com.ch.yoon.kakao.pay.imagesearch.utils;

import androidx.annotation.Nullable;

/**
 * Creator : ch-yoon
 * Date : 2019-08-09.
 */
public class IntegerUtil {

    public static boolean isSame(@Nullable Integer firstValue, @Nullable Integer secondValue) {
        if(firstValue != null && secondValue != null) {
            return firstValue.equals(secondValue);
        } else {
            return false;
        }
    }

}
