package com.ch.yoon.imagesearch.data.repository.image.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Creator : ch-yoon
 * Date : 2019-10-30
 **/
data class ImageDocument(
    val thumbnailUrl: String,
    val imageUrl: String,
    val docUrl: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(thumbnailUrl)
        parcel.writeString(imageUrl)
        parcel.writeString(docUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageDocument> {
        override fun createFromParcel(parcel: Parcel): ImageDocument {
            return ImageDocument(parcel)
        }

        override fun newArray(size: Int): Array<ImageDocument?> {
            return arrayOfNulls(size)
        }
    }
}