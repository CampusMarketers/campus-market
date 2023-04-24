package com.example.sellingapp.model

import android.os.Parcel
import android.os.Parcelable

data class ItemCategory(
    val catUid: String? = null,
    val category: String? = null,
    val catPicUrl:String?=null,
    val timeStamp:Long?=null

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(catUid)
        parcel.writeString(category)
        parcel.writeString(catPicUrl)
        parcel.writeValue(timeStamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemCategory> {
        override fun createFromParcel(parcel: Parcel): ItemCategory {
            return ItemCategory(parcel)
        }

        override fun newArray(size: Int): Array<ItemCategory?> {
            return arrayOfNulls(size)
        }
    }
}