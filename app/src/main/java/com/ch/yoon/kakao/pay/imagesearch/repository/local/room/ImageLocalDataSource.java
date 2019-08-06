package com.ch.yoon.kakao.pay.imagesearch.repository.local.room;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.dao.ImageSearchDao;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDetailInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageInfo;

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

    public void saveLocalImageDocumentList(@NonNull List<LocalImageDocument> localImageDocumentList) {
        imageSearchDao.insertAll(localImageDocumentList);
    }

    public Single<List<ImageInfo>> getImageSearchList(@NonNull ImageSearchRequest imageSearchRequest) {
        final int requiredSize = imageSearchRequest.getRequiredSize();
        final int pageNumber = imageSearchRequest.getPageNumber();

        final String keyword = imageSearchRequest.getKeyword();
        final int startNumber = (requiredSize * (pageNumber - 1)) + 1;
        final int endNumber = startNumber + imageSearchRequest.getRequiredSize() - 1;
        final String imageSortType = imageSearchRequest.getImageSortType().getType();

        return imageSearchDao.selectThumbnailInfoList(keyword, startNumber, endNumber, imageSortType);
    }

    public Single<ImageDetailInfo> getImageDetailInfo(@NonNull String id) {
        return imageSearchDao.selectImageDetailInfo(id);
    }

    public Completable deleteAllByKeyword(@NonNull String keyword) {
        return imageSearchDao.deleteSearchLog(keyword)
            .onErrorComplete()
            .andThen(imageSearchDao.deleteAll(keyword));
    }

    public Single<SearchLog> updateSearchHistory(@NonNull String keyword) {
        final long time = System.currentTimeMillis();
        SearchLog newSearchLog = new SearchLog(keyword, time);
        return imageSearchDao.update(newSearchLog)
            .toSingle(() -> newSearchLog);
    }

    public Single<List<SearchLog>> getSearchHistory() {
        return imageSearchDao.selectAllSearchLog();
    }

}
