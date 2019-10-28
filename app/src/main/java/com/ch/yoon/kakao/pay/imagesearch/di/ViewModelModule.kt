package com.ch.yoon.kakao.pay.imagesearch.di

import com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail.ImageDetailViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.ImageListViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.SearchBoxViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val viewModelModule = module {

    viewModel {
        SearchBoxViewModel(get(), get())
    }

    viewModel {
        ImageListViewModel(get(), get(), get())
    }

    viewModel {
        ImageDetailViewModel(get())
    }

}