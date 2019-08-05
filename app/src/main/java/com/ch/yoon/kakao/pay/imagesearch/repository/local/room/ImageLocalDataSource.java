package com.ch.yoon.kakao.pay.imagesearch.repository.local.room;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.dao.ImageDocumentDao;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDetailInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.RequestMeta;

import java.util.List;

import io.reactivex.Single;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageLocalDataSource {

    private final ImageDocumentDao imageDocumentDao;

    private static ImageLocalDataSource INSTANCE;

    public static synchronized ImageLocalDataSource getInstance(@NonNull ImageDocumentDao imageDocumentDao) {
        if(INSTANCE == null) {
            INSTANCE = new ImageLocalDataSource(imageDocumentDao);
        }

        return INSTANCE;
    }

    private ImageLocalDataSource(@NonNull ImageDocumentDao imageDocumentDao) {
        this.imageDocumentDao = imageDocumentDao;
    }

    public void saveLocalImageDocumentList(@NonNull List<LocalImageDocument> localImageDocumentList) {
        imageDocumentDao.insertAll(localImageDocumentList);
    }

    public Single<ImageSearchResult> getImageSearchList(@NonNull ImageSearchRequest imageSearchRequest) {
        final int requiredSize = imageSearchRequest.getRequiredSize();
        final int pageNumber = imageSearchRequest.getPageNumber();

        final String keyword = imageSearchRequest.getKeyword();
        final int startNumber = (requiredSize * (pageNumber - 1)) + 1;
        final int endNumber = startNumber + imageSearchRequest.getRequiredSize() - 1;
        final ImageSortType imageSortType = imageSearchRequest.getImageSortType();
        final String imageSortTypeStr = imageSortType.getType();
        final boolean isLastData = false;

        final RequestMeta requestMeta = new RequestMeta(isLastData, keyword, imageSortType);

        return imageDocumentDao
            .selectThumbnailInfoList(keyword, startNumber, endNumber, imageSortTypeStr)
            .map(thumbnailInfoList ->
                new ImageSearchResult(requestMeta, thumbnailInfoList)
            );
    }

    public Single<ImageDetailInfo> getImageDetailInfo(@NonNull String id) {
        return imageDocumentDao.selectImageDetailInfo(id);
    }
}
