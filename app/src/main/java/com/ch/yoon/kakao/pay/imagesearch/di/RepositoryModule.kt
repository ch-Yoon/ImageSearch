package com.ch.yoon.kakao.pay.imagesearch.di

import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageSearchRepository
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageSearchRepositoryImpl
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val repositoryModule = module {

    single<ImageSearchRepository> {
        ImageSearchRepositoryImpl(get(), get())
    }

}