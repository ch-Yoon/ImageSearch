package com.ch.yoon.kakao.pay.imagesearch.repository.local;

import androidx.annotation.NonNull;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.dao.ImageDocumentDao;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;

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

    public Single<List<LocalImageDocument>> getLocalImageDocumentList(@NonNull ImageSearchRequest imageSearchRequest) {
        final String keyword = imageSearchRequest.getKeyword();
        final int startNumber = (imageSearchRequest.getRequiredSize() * (imageSearchRequest.getPageNumber() - 1)) + 1;
        final int endNumber = startNumber + imageSearchRequest.getRequiredSize() - 1;
        final String imageSortType = imageSearchRequest.getImageSortType().getType();

        return imageDocumentDao.select(
            keyword,
            startNumber,
            endNumber,
            imageSortType
        );
    }

}
