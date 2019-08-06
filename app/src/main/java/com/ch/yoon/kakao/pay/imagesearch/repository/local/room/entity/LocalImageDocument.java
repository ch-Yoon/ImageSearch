package com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
@Entity(tableName = "documents")
public class LocalImageDocument {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String id;

    @NonNull
    @ColumnInfo(name = "keyword")
    private final String keyword;

    @ColumnInfo(name = "itemNumber")
    private final int itemNumber;

    @NonNull
    @ColumnInfo(name = "imageSortType")
    private final String imageSortType;

    @Nullable
    @ColumnInfo(name = "collection")
    private final String collection;

    @Nullable
    @ColumnInfo(name = "thumbnailUrl")
    private final String thumbnailUrl;

    @Nullable
    @ColumnInfo(name = "imageUrl")
    private final String imageUrl;

    @ColumnInfo(name = "width")
    private int width;

    @ColumnInfo(name = "height")
    private int height;

    @Nullable
    @ColumnInfo(name = "displaySiteName")
    private final String displaySiteName;

    @Nullable
    @ColumnInfo(name = "docUrl")
    private final String docUrl;

    @Nullable
    @ColumnInfo(name = "dateTime")
    private final String dateTime;

    public LocalImageDocument(@NonNull String id,
                              @NonNull String keyword,
                              int itemNumber,
                              @NonNull String imageSortType,
                              @Nullable String collection,
                              @Nullable String thumbnailUrl,
                              @Nullable String imageUrl,
                              int width,
                              int height,
                              @Nullable String displaySiteName,
                              @Nullable String docUrl,
                              @Nullable String dateTime) {
        this.id = id;
        this.keyword = keyword;
        this.itemNumber = itemNumber;
        this.imageSortType = imageSortType;
        this.collection = collection;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.width = width;
        this.height = height;
        this.displaySiteName = displaySiteName;
        this.docUrl = docUrl;
        this.dateTime = dateTime;
    }

    @NonNull
    public static String generateId(@NonNull String keyword,
                                    int itemNumber,
                                    @NonNull String imageSortType) {
        return keyword + "," + itemNumber + "," + imageSortType;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getKeyword() {
        return keyword;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    @NonNull
    public String getImageSortType() {
        return imageSortType;
    }

    @Nullable
    public String getCollection() {
        return collection;
    }

    @Nullable
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

}
