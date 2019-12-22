package com.ch.yoon.imagesearch.di

import androidx.room.Room
import com.ch.yoon.local.AppDatabase
import com.ch.yoon.local.ImageLocalDataSourceImpl
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogLocalDataSource
import com.ch.yoon.local.SearchLogLocalDataSourceImpl
import com.ch.yoon.imagesearch.data.repository.image.ImageLocalDataSource
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val databaseModule = module {

    single<ImageLocalDataSource> {
        com.ch.yoon.local.ImageLocalDataSourceImpl(get())
    }

    single<SearchLogLocalDataSource> {
        com.ch.yoon.local.SearchLogLocalDataSourceImpl(get())
    }

    single {
        get<com.ch.yoon.local.AppDatabase>().imageDAO()
    }

    single {
        get<com.ch.yoon.local.AppDatabase>().searchLogDao()
    }

    single {
        Room.databaseBuilder(get(), com.ch.yoon.local.AppDatabase::class.java, "app_database").build()
    }


}