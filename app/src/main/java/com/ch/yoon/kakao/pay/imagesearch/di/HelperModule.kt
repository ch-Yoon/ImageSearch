package com.ch.yoon.kakao.pay.imagesearch.di

import com.ch.yoon.kakao.pay.imagesearch.ui.common.pageload.PageLoadConfiguration
import com.ch.yoon.kakao.pay.imagesearch.ui.common.pageload.PageLoadInspector
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val helperModule = module {

    factory {
        PageLoadInspector<String>(get())
    }

    factory {
        PageLoadConfiguration(1, 50, 50, 5)
    }

}