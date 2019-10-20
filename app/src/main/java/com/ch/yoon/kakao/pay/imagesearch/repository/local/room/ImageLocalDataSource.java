package com.ch.yoon.kakao.pay.imagesearch.repository.local.room;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.dao.ImageSearchDao;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageLocalDataSource {

    private final ImageSearchDao imageSearchDao;

    private static ImageLocalDataSource INSTANCE;

    public static synchronized ImageLocalDataSource getInstance(@NonNull ImageSearchDao imageSearchDao) {
        if(INSTANCE == null) {
            INSTANCE = new ImageLocalDataSource(imageSearchDao);
        }

        return INSTANCE;
    }

    private ImageLocalDataSource(@NonNull ImageSearchDao imageSearchDao) {
        this.imageSearchDao = imageSearchDao;
    }

    @NonNull
    public Completable deleteSearchLog(@NonNull String keyword) {
        return imageSearchDao.deleteSearchLog(keyword);
    }

    @NonNull
    public Single<SearchLog> insertOrUpdate(@NonNull String keyword) {
        final SearchLog newSearchLog = new SearchLog(
            keyword,
            System.currentTimeMillis()
        );

        return imageSearchDao.insertOrUpdate(newSearchLog);
    }

    @NonNull
    public Single<List<SearchLog>> getSearchLogList() {
        return imageSearchDao.selectAllSearchLog();
    }

}
