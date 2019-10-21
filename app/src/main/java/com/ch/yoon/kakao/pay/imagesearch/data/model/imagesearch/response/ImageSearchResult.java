package com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest;

import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-10-21.
 */
public class ImageSearchResult {

    @NonNull
    private final ImageSearchRequest imageSearchRequest;

    @Nullable
    private final SearchMetaInfo searchMetaInfo;

    @NonNull
    private final List<ImageDocument> imageDocumentList;

    public ImageSearchResult(@NonNull ImageSearchRequest imageSearchRequest,
                             @Nullable SearchMetaInfo searchMetaInfo,
                             @NonNull List<ImageDocument> imageDocumentList) {
        this.imageSearchRequest = imageSearchRequest;
        this.searchMetaInfo = searchMetaInfo;
        this.imageDocumentList = imageDocumentList;
    }

    @NonNull
    public ImageSearchRequest getImageSearchRequest() {
        return imageSearchRequest;
    }

    @Nullable
    public SearchMetaInfo getSearchMetaInfo() {
        return searchMetaInfo;
    }

    @NonNull
    public List<ImageDocument> getImageDocumentList() {
        return imageDocumentList;
    }

}
