package com.ch.yoon.kakao.pay.imagesearch.repository.local.entity;

import androidx.annotation.NonNull;
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
    private String id;

    @ColumnInfo(name = "keyword")
    private String keyword;

    @ColumnInfo(name = "number")
    private int number;

    @ColumnInfo(name = "image_sort_type")
    private String imageSortType;

    @ColumnInfo(name = "collection")
    private String collection;

    @ColumnInfo(name = "thumb_name")
    private String thumbnailUrl;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "width")
    private int width;

    @ColumnInfo(name = "height")
    private int height;

    @ColumnInfo(name = "display_site_name")
    private String displaySiteName;

    @ColumnInfo(name = "doc_url")
    private String docUrl;

    @ColumnInfo(name = "date_time")
    private String dateTime;

    public LocalImageDocument(@NonNull String keyword,
                              int number,
                              String imageSortType,
                              String collection,
                              String thumbnailUrl,
                              String imageUrl,
                              int width,
                              int height,
                              String displaySiteName,
                              String docUrl,
                              String dateTime) {
        id = keyword + "," + number + "," + imageSortType;
        this.keyword = keyword;
        this.number = number;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getNumber() {
        return number;
    }

    public String getImageSortType() {
        return imageSortType;
    }

    public String getCollection() {
        return collection;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDisplaySiteName() {
        return displaySiteName;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

}
