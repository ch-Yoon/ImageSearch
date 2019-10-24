package com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public final class ImageDocument implements Parcelable {

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

    public ImageDocument(@Nullable String collection,
                         @Nullable String thumbnailUrl,
                         @Nullable String imageUrl,
                         int width,
                         int height,
                         @Nullable String displaySiteName,
                         @Nullable String docUrl,
                         @Nullable String dateTime) {
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

    @NonNull
    @Override
    public String toString() {
        return "ImageDocument{" +
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

    protected ImageDocument(Parcel in) {
        this.collection = in.readString();
        this.thumbnailUrl = in.readString();
        this.imageUrl = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.displaySiteName = in.readString();
        this.docUrl = in.readString();
        this.dateTime = in.readString();
    }

    public static final Creator<ImageDocument> CREATOR = new Creator<ImageDocument>() {
        @Override
        public ImageDocument createFromParcel(Parcel source) {
            return new ImageDocument(source);
        }

        @Override
        public ImageDocument[] newArray(int size) {
            return new ImageDocument[size];
        }
    };

}
