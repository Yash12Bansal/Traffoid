package com.example.routesuggestor

import android.os.Parcel
import android.os.Parcelable
@Parcelize
data class AddressResult(var data:String? =null,var success:Boolean=false):Parcelable {
    private  val TAG = "AddressResult"
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(data)
        parcel.writeByte(if (success) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressResult> {
        override fun createFromParcel(parcel: Parcel): AddressResult {
            return AddressResult(parcel)
        }

        override fun newArray(size: Int): Array<AddressResult?> {
            return arrayOfNulls(size)
        }
    }
}