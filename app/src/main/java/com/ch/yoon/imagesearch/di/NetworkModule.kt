package com.ch.yoon.imagesearch.di

import com.ch.yoon.imagesearch.BuildConfig
import com.ch.yoon.remote.kakao.KakaoSearchApi
import com.ch.yoon.imagesearch.data.repository.image.ImageRemoteDataSource
import com.ch.yoon.remote.kakao.ImageRemoteDataSourceImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
val networkModule = module {

    single<ImageRemoteDataSource> {
        com.ch.yoon.remote.kakao.ImageRemoteDataSourceImpl(get())
    }

    single {
        get<Retrofit>().create(com.ch.yoon.remote.kakao.KakaoSearchApi::class.java)
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
    }

    single {
        OkHttpClient.Builder().apply {
            addInterceptor(get())
            addInterceptor(HttpLoggingInterceptor().apply {
                if(BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
        }.build()
    }

    single {
        Interceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .header("Authorization", BuildConfig.KAKAO_API_KEY)
                    .build())
        }
    }

}