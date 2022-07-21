package com.example.routesuggestor

import android.os.Parcel
import android.os.Parcelable

data class Locations(var currlatitude:Double,var currlongitude:Double,var deslatitude:Double,var deslongitude:Double):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(currlatitude)
        parcel.writeDouble(currlongitude)
        parcel.writeDouble(deslatitude)
        parcel.writeDouble(deslongitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Locations> {
        override fun createFromParcel(parcel: Parcel): Locations {
            return Locations(parcel)
        }

        override fun newArray(size: Int): Array<Locations?> {
            return arrayOfNulls(size)
        }
    }
}