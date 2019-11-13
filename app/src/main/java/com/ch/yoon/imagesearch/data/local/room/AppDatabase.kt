package com.ch.yoon.imagesearch.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ch.yoon.imagesearch.data.local.room.dao.ImageDAO
import com.ch.yoon.imagesearch.data.local.room.dao.SearchLogDAO
import com.ch.yoon.imagesearch.data.local.room.entity.ImageDocumentEntity
import com.ch.yoon.imagesearch.data.local.room.entity.SearchLogEntity

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Database(entities = [SearchLogEntity::class, ImageDocumentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchLogDao(): SearchLogDAO

    abstract fun ImageDAO(): ImageDAO

}