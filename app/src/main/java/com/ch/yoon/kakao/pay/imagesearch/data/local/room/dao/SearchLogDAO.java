package com.ch.yoon.kakao.pay.imagesearch.data.local.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
@Dao
public interface SearchLogDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrUpdateSearchLog(SearchLog searchLog);

    @Query("SELECT * FROM searchLogs")
    Single<List<SearchLog>> selectAllSearchLog();

    @Query("DELETE FROM searchLogs WHERE keyword = :keyword")
    Completable deleteSearchLog(String keyword);

}
