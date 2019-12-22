package com.ch.yoon.data.model.image.response

import android.os.Parcel
import android.os.Parcelable

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
data class ImageDocument(
    val id: String,
    val collection: String,
    val thumbnailUrl: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val displaySiteName: String,
    val docUrl: String,
    val dateTime: String,
    var isFavorite: Boolean
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readInt(),
        source.readInt(),
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readInt() == 1
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(collection)
        writeString(thumbnailUrl)
        writeString(imageUrl)
        writeInt(width)
        writeInt(height)
        writeString(displaySiteName)
        writeString(docUrl)
        writeString(dateTime)
        writeInt((if (isFavorite) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImageDocument> = object : Parcelable.Creator<ImageDocument> {
            override fun createFromParcel(source: Parcel): ImageDocument = ImageDocument(source)
            override fun newArray(size: Int): Array<ImageDocument?> = arrayOfNulls(size)
        }
    }
}