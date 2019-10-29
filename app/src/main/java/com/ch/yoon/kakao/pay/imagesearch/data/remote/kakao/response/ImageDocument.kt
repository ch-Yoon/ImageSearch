package com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Creator : ch-yoon
 * Date : 2019-10-29
 **/
data class ImageDocument(
    @SerializedName("collection") val collection: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("display_sitename") val displaySiteName: String,
    @SerializedName("doc_url") val docUrl: String,
    @SerializedName("dateTime") val dateTime: String
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readInt(),
        source.readInt(),
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(collection)
        writeString(thumbnailUrl)
        writeString(imageUrl)
        writeInt(width)
        writeInt(height)
        writeString(displaySiteName)
        writeString(docUrl)
        writeString(dateTime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImageDocument> = object : Parcelable.Creator<ImageDocument> {
            override fun createFromParcel(source: Parcel): ImageDocument = ImageDocument(source)
            override fun newArray(size: Int): Array<ImageDocument?> = arrayOfNulls(size)
        }
    }

}