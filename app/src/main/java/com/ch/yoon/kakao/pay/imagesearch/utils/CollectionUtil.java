package com.ch.yoon.kakao.pay.imagesearch.utils;

import java.util.Collection;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class CollectionUtil {
    
    public static boolean isEmpty(Collection collection) {
        return (collection == null) || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }
}
