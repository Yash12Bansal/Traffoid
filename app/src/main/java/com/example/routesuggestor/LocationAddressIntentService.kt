package com.example.routesuggestor

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.IOException
import java.lang.Exception
import java.util.*

const val LOCATION_DATA_EXTRA="LOCATION_DATA_EXTRA"
const val BROADCAST_ACTION_LOCATION_ADDRESS="BROADCAST_ACTION_LOCATION_ADDRESS"

class LocationAddressIntentService :IntentService("LocationAddressIntentService"){
    private  val TAG = "LocationAddressIntentSe"
//    Log.e(TAG, "onHandleIntent: dfdfdffffffffffffffffffffffffffffffffffffffffffffffffffffff" )

    override fun onHandleIntent(intent: Intent?) {
        println("thissssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss")

        intent?:return
        println("dfdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")


        Log.e(TAG, "onHandleIntent: dfdfdffffffffffffffffffffffffffffffffffffffffffffffffffffff" )
        var latLong:LatLong?=intent.getParcelableExtra(LOCATION_DATA_EXTRA)?:return


        latLong?:return

        var addressResult=AddressResult()

        var geocoder= Geocoder(this, Locale.getDefault())
        var locationAddresses:List<Address> = emptyList()
        try{
            locationAddresses=geocoder.getFromLocation(latLong.latitude,latLong.longitude,1)
        }
        catch (e:IOException){
            addressResult.data="Error occured a"

        }
        catch(ex:Exception){
            addressResult.data="any exception"
        }

        if(locationAddresses.isNotEmpty()){
            var address=locationAddresses[0]
            println(address)
            var addressTokens=with(address){
                (0..maxAddressLineIndex).map{getAddressLine(it)}
            }
            addressResult.data=addressTokens.joinToString("\n")
            addressResult.success=true

            println("dfdffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+address)

        }
//        else{
//            addressResult.data="haidafidififi"
//            addressResult.success=true
//
//        }
        broadcastStatus(addressResult)

    }

    private fun broadcastStatus(addressResult: AddressResult) {
        Intent().also{ intent->
            intent.action= BROADCAST_ACTION_LOCATION_ADDRESS
            intent.putExtra(LOCATION_DATA_EXTRA,addressResult)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }

    }


}