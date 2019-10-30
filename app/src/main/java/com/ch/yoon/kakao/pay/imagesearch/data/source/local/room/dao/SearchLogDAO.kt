package com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.entity.SearchLogModel
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Dao
interface SearchLogDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateSearchLog(searchLogModel: SearchLogModel): Completable

    @Query("SELECT * FROM searchLogs")
    fun selectAllSearchLog(): Single<List<SearchLogModel>>

    @Query("DELETE FROM searchLogs WHERE keyword = :keyword AND time = :time")
    fun deleteSearchLog(keyword: String, time: Long): Completable

}