package com.ch.yoon.kakao.pay.imagesearch.di

import androidx.room.Room
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.AppDatabase
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageSearchLocalDataSource
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageSearchLocalDataSourceImpl
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val databaseModule = module {

    single<ImageSearchLocalDataSource> {
        ImageSearchLocalDataSourceImpl(get())
    }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database").build()
    }

    single {
        get<AppDatabase>().searchLogDao()
    }

}