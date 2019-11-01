package com.ch.yoon.kakao.pay.imagesearch.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.dao.SearchLogDAO
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLogModel

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Database(entities = [SearchLogModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchLogDao(): SearchLogDAO

}