package com.example.routesuggestor

import android.os.Parcel
import android.os.Parcelable

@Parcelize
data class LatLong(var latitude:Double,var longitude:Double):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LatLong> {
        override fun createFromParcel(parcel: Parcel): LatLong {
            return LatLong(parcel)
        }

        override fun newArray(size: Int): Array<LatLong?> {
            return arrayOfNulls(size)
        }
    }
}

annotation class Parcelize
