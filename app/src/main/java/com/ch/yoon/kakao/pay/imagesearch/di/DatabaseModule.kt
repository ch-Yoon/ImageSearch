package com.ch.yoon.kakao.pay.imagesearch.di

import androidx.room.Room
import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.AppDatabase
import com.ch.yoon.kakao.pay.imagesearch.data.repository.image.ImageLocalDataSource
import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.ImageLocalDataSourceImpl
import org.koin.dsl.module.module

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val databaseModule = module {

    single<ImageLocalDataSource> {
        ImageLocalDataSourceImpl(get())
    }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database").build()
    }

    single {
        get<AppDatabase>().searchLogDao()
    }

}