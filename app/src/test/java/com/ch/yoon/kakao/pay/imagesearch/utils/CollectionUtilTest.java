package com.ch.yoon.kakao.pay.imagesearch.utils;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Creator : ch-yoon
 * Date : 2019-08-13.
 */
public class CollectionUtilTest {

    @Test
    public void List가_null일때_비어있다고_판단하는지_테스트() {
        // when
        boolean isEmpty = CollectionUtil.isEmpty(null);
        boolean isNotEmpty = CollectionUtil.isNotEmpty(null);

        // then
        assertEquals(isEmpty, true);
        assertEquals(isNotEmpty, false);
    }

    @Test
    public void List의_사이즈가_0일때_비어있다고_판단하는지_테스트() {
        // when
        boolean isEmpty = CollectionUtil.isEmpty(new ArrayList<Integer>());
        boolean isNotEmpty = CollectionUtil.isNotEmpty(new ArrayList<Integer>());

        // then
        assertEquals(isEmpty, true);
        assertEquals(isNotEmpty, false);
    }

    @Test
    public void List안에_값이_있을때_비어있지_않다고_판단하는지_테스트() {
        // given
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);

        // when
        boolean isEmpty = CollectionUtil.isEmpty(list);
        boolean isNotEmpty = CollectionUtil.isNotEmpty(list);

        // then
        assertEquals(isEmpty, false);
        assertEquals(isNotEmpty, true);
    }

}
