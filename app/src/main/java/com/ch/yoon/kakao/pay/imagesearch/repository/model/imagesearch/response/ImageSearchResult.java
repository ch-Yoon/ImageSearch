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
    private final List<SimpleImageInfo> simpleImageInfoList;

    public ImageSearchResult(@NonNull RequestMeta requestMeta,
                             @NonNull List<SimpleImageInfo> simpleImageInfoList) {
        this.requestMeta = requestMeta;
        this.simpleImageInfoList = simpleImageInfoList;
    }

    @NonNull
    public RequestMeta getRequestMeta() {
        return requestMeta;
    }

    @NonNull
    public List<SimpleImageInfo> getSimpleImageInfoList() {
        return simpleImageInfoList;
    }


    @Override
    public String toString() {
        return "ImageSearchResult{" +
            "requestMeta=" + requestMeta +
            ", simpleImageInfoList=" + simpleImageInfoList +
            '}';
    }

}
