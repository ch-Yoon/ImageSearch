package com.ch.yoon.kakao.pay.imagesearch.di

import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val helperModule = module {

    factory {
        ImageSearchInspector(1, 50, 80, 20)
    }

}