package com.ch.yoon.kakao.pay.imagesearch.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Creator : ch-yoon
 * Date : 2019-10-28
 **/
@Dao
interface SearchLogDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateSearchLog(searchLog: SearchLog): Completable

    @Query("SELECT * FROM searchLogs")
    fun selectAllSearchLog(): Single<List<SearchLog>>

    @Query("DELETE FROM searchLogs WHERE keyword = :keyword")
    fun deleteSearchLog(keyword: String): Completable

}