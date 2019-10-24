package com.ch.yoon.kakao.pay.imagesearch.utils;

import androidx.annotation.Nullable;

import java.util.Collection;

/**
 * Creator : ch-yoon
 * Date : 2019-08-04.
 */
public class CollectionUtil {

    public static boolean isNotEmpty(@Nullable final Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(@Nullable final Collection collection) {
        return collection == null || collection.size() == 0;
    }

}
