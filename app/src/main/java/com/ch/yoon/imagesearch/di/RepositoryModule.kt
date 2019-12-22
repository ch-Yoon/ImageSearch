package com.ch.yoon.imagesearch.di

import com.ch.yoon.imagesearch.data.repository.image.ImageRepository
import com.ch.yoon.data.repository.ImageRepositoryImpl
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogRepository
import com.ch.yoon.data.repository.SearchLogRepositoryImpl
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val repositoryModule = module {

    single<ImageRepository> {
        com.ch.yoon.data.repository.ImageRepositoryImpl(get(), get())
    }

    single<SearchLogRepository> {
        com.ch.yoon.data.repository.SearchLogRepositoryImpl(get())
    }
}