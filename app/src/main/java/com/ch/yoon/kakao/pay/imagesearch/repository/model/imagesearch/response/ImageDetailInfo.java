package com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;

/**
 * Creator : ch-yoon
 * Date : 2019-08-06.
 */
public class ImageDetailInfo {

    @Nullable
    private final String imageUrl;

    @Nullable
    private final String displaySiteName;

    @Nullable
    private final String docUrl;

    @Nullable
    private final String dateTime;

    private int width;
    private int height;

    public ImageDetailInfo(@Nullable String imageUrl,
                           @Nullable String displaySiteName,
                           @Nullable String docUrl,
                           @Nullable String dateTime,
                           int width,
                           int height) {
        this.imageUrl = imageUrl;
        this.displaySiteName = displaySiteName;
        this.docUrl = docUrl;
        this.dateTime = dateTime;
        this.width = width;
        this.height = height;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    @Nullable
    public String getDisplaySiteName() {
        return displaySiteName;
    }

    @Nullable
    public String getDocUrl() {
        return docUrl;
    }

    @Nullable
    public String getDateTime() {
        return dateTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
