package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.LocalImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.RequestMeta;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.SearchMetaInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageInfoConverter {

    public static List<LocalImageDocument> toLocalImageDocumentList(@NonNull ImageSearchRequest imageSearchRequest,
                                                                    @NonNull List<ImageDocument> imageDocumentList) {
        final String keyword = imageSearchRequest.getKeyword();
        final int pageNumber = imageSearchRequest.getPageNumber();
        final int requiredDataSize = imageSearchRequest.getRequiredSize();
        final String imageSortType = imageSearchRequest.getImageSortType().getType();

        final List<LocalImageDocument> localImageDocumentList = new ArrayList<>();
        for (int i = 0; i < imageDocumentList.size(); i++) {
            final ImageDocument imageDocument = imageDocumentList.get(i);
            final int itemNumber = ((pageNumber-1) * requiredDataSize) + i + 1;
            final String id = LocalImageDocument.generateId(keyword, itemNumber, imageSortType);

            final LocalImageDocument localImageDocument = new LocalImageDocument(
                id,
                keyword,
                itemNumber,
                imageSortType,
                imageDocument.getCollection(),
                imageDocument.getThumbnailUrl(),
                imageDocument.getImageUrl(),
                imageDocument.getWidth(),
                imageDocument.getHeight(),
                imageDocument.getDisplaySiteName(),
                imageDocument.getDocUrl(),
                imageDocument.getDateTime()
            );

            localImageDocumentList.add(localImageDocument);
        }

        return localImageDocumentList;
    }

    public static ImageSearchResult toImageSearchResult(@NonNull ImageSearchRequest imageSearchRequest,
                                                        @Nullable SearchMetaInfo searchMetaInfo,
                                                        @NonNull List<LocalImageDocument> localImageDocumentList) {
        final boolean isEnd = searchMetaInfo == null || searchMetaInfo.isEnd();
        final String keyword = imageSearchRequest.getKeyword();
        final ImageSortType imageSortType = imageSearchRequest.getImageSortType();
        final RequestMeta requestMeta = new RequestMeta(isEnd, keyword, imageSortType);

        final List<ImageInfo> imageInfoList = new ArrayList<>();
        for(LocalImageDocument document : localImageDocumentList) {
            final String uniqueInfo = document.getId();
            final String thumbnailUrl = document.getThumbnailUrl();

            imageInfoList.add(new ImageInfo(uniqueInfo, thumbnailUrl));
        }

        return new ImageSearchResult(requestMeta, imageInfoList);
    }

}
