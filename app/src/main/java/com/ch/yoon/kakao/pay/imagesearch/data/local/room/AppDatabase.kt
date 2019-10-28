package com.ch.yoon.kakao.pay.imagesearch.data.local.room

import androidx.room.RoomDatabase
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.dao.SearchLogDAO

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchLogDao(): SearchLogDAO

}