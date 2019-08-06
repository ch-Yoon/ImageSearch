package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class ImageSearchResult {

    @NonNull
    private final RequestMeta requestMeta;

    @NonNull
    private final List<ImageInfo> imageInfoList;

    public ImageSearchResult(@NonNull RequestMeta requestMeta,
                             @NonNull List<ImageInfo> imageInfoList) {
        this.requestMeta = requestMeta;
        this.imageInfoList = imageInfoList;
    }

    @NonNull
    public RequestMeta getRequestMeta() {
        return requestMeta;
    }

    @NonNull
    public List<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }
}
