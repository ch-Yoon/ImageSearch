package com.ch.yoon.kakao.pay.imagesearch.di

import androidx.room.Room
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageDatabase
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageLocalDataSource
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageLocalDataSourceImpl
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
        Room.databaseBuilder(get(), ImageDatabase::class.java, "image_database").build()
    }

    single {
        get<ImageDatabase>().searchLogDao()
    }

}