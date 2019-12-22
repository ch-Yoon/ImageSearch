package com.ch.yoon.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ch.yoon.local.room.dao.ImageDAO
import com.ch.yoon.local.room.dao.SearchLogDAO
import com.ch.yoon.local.room.model.LocalImageDocument
import com.ch.yoon.local.room.model.LocalSearchLog

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Database(entities = [LocalSearchLog::class, LocalImageDocument::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchLogDao(): SearchLogDAO

    abstract fun imageDAO(): ImageDAO

}