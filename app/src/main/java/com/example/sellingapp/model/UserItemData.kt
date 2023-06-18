package com.example.sellingapp.model

import android.os.Parcel
import android.os.Parcelable

data class UserItemData(
    val downloadedImageUrl: String? = null,
    val itemName: String? = null,
    val userUid: String? = null,
    val itemId: String? = null,
    val itemDescription: String? = null,
    val itemTimeStamp: Long? = null,
    val itemPrice: String? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(downloadedImageUrl)
        parcel.writeString(itemName)
        parcel.writeString(userUid)
        parcel.writeString(itemId)
        parcel.writeString(itemDescription)
        parcel.writeValue(itemTimeStamp)
        parcel.writeString(itemPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserItemData> {
        override fun createFromParcel(parcel: Parcel): UserItemData {
            return UserItemData(parcel)
        }

        override fun newArray(size: Int): Array<UserItemData?> {
            return arrayOfNulls(size)
        }
    }
}
