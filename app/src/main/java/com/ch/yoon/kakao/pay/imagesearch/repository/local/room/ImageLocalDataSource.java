package com.ch.yoon.kakao.pay.imagesearch.repository.local.room;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.dao.ImageSearchDao;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.DetailImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.SimpleImageInfo;

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

    public Single<List<SimpleImageInfo>> getImageSearchList(@NonNull ImageSearchRequest imageSearchRequest) {
        final int requiredSize = imageSearchRequest.getRequiredSize();
        final int pageNumber = imageSearchRequest.getPageNumber();

        final String keyword = imageSearchRequest.getKeyword();
        final int startNumber = (requiredSize * (pageNumber - 1)) + 1;
        final int endNumber = startNumber + imageSearchRequest.getRequiredSize() - 1;
        final String imageSortType = imageSearchRequest.getImageSortType().getType();

        return imageSearchDao.selectSimpleImageInfoList(keyword, startNumber, endNumber, imageSortType);
    }

    public Single<DetailImageInfo> getImageDetailInfo(@NonNull String id) {
        return imageSearchDao.selectDetailImageInfo(id);
    }

    public Completable deleteAllByKeyword(@NonNull String keyword) {
        return imageSearchDao.deleteSearchLog(keyword)
            .onErrorComplete()
            .andThen(imageSearchDao.deleteAllDocument(keyword));
    }

    public Single<SearchLog> updateSearchLog(@NonNull String keyword) {
        final long time = System.currentTimeMillis();
        SearchLog newSearchLog = new SearchLog(keyword, time);
        return imageSearchDao.update(newSearchLog)
            .toSingle(() -> newSearchLog);
    }

    public Single<List<SearchLog>> getSearchLogList() {
        return imageSearchDao.selectAllSearchLog();
    }

}
