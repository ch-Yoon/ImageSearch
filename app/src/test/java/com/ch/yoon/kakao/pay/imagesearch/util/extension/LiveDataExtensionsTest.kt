package com.ch.yoon.kakao.pay.imagesearch.util.extension

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
    fun `LiveData가 update되는지 테스트`() {
        // given
        val liveData = MutableLiveData<String>("first")

        // when
        liveData.updateOnMainThread { beforeValue ->
            "${beforeValue}Text"
        }

        // then
        assertEquals("firstText", liveData.value)
    }
}