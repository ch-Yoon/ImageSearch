package com.ch.yoon.imagesearch.di

import com.ch.yoon.imagesearch.presentation.common.pageload.PageLoadConfiguration
import com.ch.yoon.imagesearch.presentation.common.pageload.PageLoadHelper
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val helperModule = module {

    factory {
        PageLoadHelper<String>(get())
    }

    factory {
        PageLoadConfiguration(1, 50, 50, 5)
    }

}