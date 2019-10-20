package com.ch.yoon.kakao.pay.imagesearch.data.local.room;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.data.local.room.dao.SearchLogDao;
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageLocalDataSource {

    private final SearchLogDao searchLogDao;

    private static ImageLocalDataSource INSTANCE;

    public static synchronized ImageLocalDataSource getInstance(@NonNull SearchLogDao searchLogDao) {
        if(INSTANCE == null) {
            INSTANCE = new ImageLocalDataSource(searchLogDao);
        }

        return INSTANCE;
    }

    private ImageLocalDataSource(@NonNull SearchLogDao searchLogDao) {
        this.searchLogDao = searchLogDao;
    }

    @NonNull
    public Single<SearchLog> insertOrUpdateSearchLog(@NonNull String keyword) {
        final SearchLog newSearchLog = new SearchLog(keyword, System.currentTimeMillis());
        return searchLogDao.insertOrUpdateSearchLog(newSearchLog)
            .toSingle(() -> newSearchLog);
    }

    @NonNull
    public Completable deleteSearchLog(@NonNull String keyword) {
        return searchLogDao.deleteSearchLog(keyword);
    }

    @NonNull
    public Single<List<SearchLog>> selectAllSearchLog() {
        return searchLogDao.selectAllSearchLog();
    }

}
