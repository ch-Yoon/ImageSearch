package com.ch.yoon.imagesearch.di

import androidx.room.Room
import com.ch.yoon.imagesearch.data.local.room.AppDatabase
import com.ch.yoon.imagesearch.data.local.room.ImageLocalDataSourceImpl
import com.ch.yoon.imagesearch.data.repository.searchlog.SearchLogLocalDataSource
import com.ch.yoon.imagesearch.data.local.room.SearchLogLocalDataSourceImpl
import com.ch.yoon.imagesearch.data.repository.image.ImageLocalDataSource
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val databaseModule = module {

    single<ImageLocalDataSource> {
        ImageLocalDataSourceImpl(get())
    }

    single<SearchLogLocalDataSource> {
        SearchLogLocalDataSourceImpl(get())
    }

    single {
        get<AppDatabase>().ImageDAO()
    }

    single {
        get<AppDatabase>().searchLogDao()
    }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database").build()
    }


}