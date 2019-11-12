package com.ch.yoon.imagesearch.di

import com.ch.yoon.imagesearch.data.repository.ImageRepository
import com.ch.yoon.imagesearch.data.repository.ImageRepositoryImpl
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val repositoryModule = module {

    single<ImageRepository> {
        ImageRepositoryImpl(get(), get())
    }

}