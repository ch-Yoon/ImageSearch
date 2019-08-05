package com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageInfo implements Parcelable {

    @Nullable
    @SerializedName("collection")
    private final String collection;

    @Nullable
    @SerializedName("thumbnail_url")
    private final String thumbnailUrl;

    @Nullable
    @SerializedName("image_url")
    private final String imageUrl;

    @SerializedName("width")
    private final int width;

    @SerializedName("height")
    private final int height;

    @Nullable
    @SerializedName("display_sitename")
    private final String displaySiteName;

    @Nullable
    @SerializedName("doc_url")
    private final String docUrl;

    @Nullable
    @SerializedName("dateTime")
    private final String dateTime;

    public ImageInfo(@NonNull String collection,
                     @NonNull String thumbnailUrl,
                     @NonNull String imageUrl,
                     int width,
                     int height,
                     @NonNull String displaySiteName,
                     @NonNull String docUrl,
                     @NonNull String dateTime) {
        this.collection = collection;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.width = width;
        this.height = height;
        this.displaySiteName = displaySiteName;
        this.docUrl = docUrl;
        this.dateTime = dateTime;
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

    @Override
    public String toString() {
        return "ImageInfo{" +
            "collection='" + collection + '\'' +
            ", thumbnailUrl='" + thumbnailUrl + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", width=" + width +
            ", height=" + height +
            ", displaySiteName='" + displaySiteName + '\'' +
            ", docUrl='" + docUrl + '\'' +
            ", dateTime='" + dateTime + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageInfo imageInfo = (ImageInfo) o;
        return width == imageInfo.width &&
            height == imageInfo.height &&
            Objects.equals(collection, imageInfo.collection) &&
            Objects.equals(thumbnailUrl, imageInfo.thumbnailUrl) &&
            Objects.equals(imageUrl, imageInfo.imageUrl) &&
            Objects.equals(displaySiteName, imageInfo.displaySiteName) &&
            Objects.equals(docUrl, imageInfo.docUrl) &&
            Objects.equals(dateTime, imageInfo.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, thumbnailUrl, imageUrl, width, height, displaySiteName, docUrl, dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.collection);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.displaySiteName);
        dest.writeString(this.docUrl);
        dest.writeString(this.dateTime);
    }

    protected ImageInfo(Parcel in) {
        this.collection = in.readString();
        this.thumbnailUrl = in.readString();
        this.imageUrl = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.displaySiteName = in.readString();
        this.docUrl = in.readString();
        this.dateTime = in.readString();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel source) {
            return new ImageInfo(source);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

}
