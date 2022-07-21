package com.example.routesuggestor
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*
// GeeksforGeeks coordinates

lateinit var originLatitude:Any
lateinit var originLongitude:Any
lateinit var destinationLatitude:Any
lateinit var destinationLongitude:Any

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    lateinit var latitude:TextView
//    lateinit var longitude:TextView
    lateinit var place:TextView
    lateinit var route:Button
    private val TAG = "MainActivity"
    lateinit var image:ImageView
    val AUTOCOMPLETE_REQUEST_CODE = 1


    lateinit var destination: EditText
//    lateinit var tv1:TextView;
//    lateinit var tv2:TextView;
    var LOCATIONS="LOCATIONS"

    private val broadcastReceiver=object:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                BROADCAST_ACTION_LOCATION_ADDRESS -> {
                    var addressResult:AddressResult?=intent.getParcelableExtra(LOCATION_DATA_EXTRA)
                    addressResult?.let{
                        if(it.success){
                            Log.e(TAG, "onReceive: dfffffffffffffffffffffffffffffffffffffffffffffffffd")
                            place.append(it.data?:"Address not found")
                        }
                        else{
                            place.append(addressResult.data)
                        }
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        image = findViewById(R.id.imageView)

        // Adding the gif here using glide library
//        Glide.with(this).load(R.drawable.bann).into(image)
        println("thissssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss")
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
//        latitude=findViewById(R.id.latitude)
//        longitude=findViewById(R.id.longitude)
        place=findViewById(R.id.place)
//        place.text="Address: "
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            BROADCAST_ACTION_LOCATION_ADDRESS))

        destination=findViewById(R.id.destination)
//        tv1=findViewById(R.id.tv1)
//        tv2=findViewById(R.id.tv2)

        Places.initialize(applicationContext,"AIzaSyBvU2OWEPxdhGm5zfR37TmtNV7csEtXd-4")
        destination.isFocusable=false
        destination.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {

                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                val fields = listOf(Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG)

                // Start the autocomplete intent.
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(applicationContext)
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

//                var fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME)
//                var intent=Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(applicationContext)
//                startActivityForResult(intent,100)
            }

        })

        route=findViewById(R.id.route)

        route.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                if(destination.text.toString()!=""){
                    var intent=Intent(applicationContext,RouteActivity::class.java)
                    startActivity(intent)

                }
                else{
                    Toast.makeText(applicationContext,"Enter Destination",Toast.LENGTH_LONG).show()
                }
            }

        })

        getCurrentLocation()



    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==AUTOCOMPLETE_REQUEST_CODE && resultCode== RESULT_OK){
            var place= Autocomplete.getPlaceFromIntent(data)
            destination.setText(place.name)
//            tv1.setText(String.format(place.address))
//            tv2.setText((place.latLng).toString())
            destinationLatitude=place.latLng.latitude.toDouble()
            destinationLongitude=place.latLng.longitude.toDouble()

        }
        else if(resultCode==AutocompleteActivity.RESULT_ERROR){
            var status=Autocomplete.getStatusFromIntent(data)
            Toast.makeText(applicationContext,status.statusMessage,Toast.LENGTH_LONG).show()

        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    fun getCurrentLocation(){
        if(checkPermission()){
            if(isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){task->
                    if(task.isSuccessful()) {
                        task.result?.let{
                            var pos=LatLong(it.latitude,it.longitude)
//                            latitude.text= pos.latitude.toString()
//                            longitude.text= pos.longitude.toString()
                            originLatitude=pos.latitude.toDouble()
                            originLongitude=pos.longitude.toDouble()

//                            longitude.setText( pos.longitude.toString())
                            fetchAddressFromLocation(pos)
                        }
                    }
                    else{

                        Log.e(TAG, "getCurrentLocation: erorroororoeroererohog fodfodfodfodfdf", )
                    }
                }

            }
            else{
                Toast.makeText(this,"Turn on location",Toast.LENGTH_LONG).show()
                var i= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)

            }

        }
        else{
            requestPermission()

        }
    }
    fun fetchAddressFromLocation(pos : LatLong){
        var intent=Intent(this,LocationAddressIntentService::class.java).apply{
            putExtra(LOCATION_DATA_EXTRA,pos)

        }
        Log.e(TAG, "fetchAddressFromLocation: erororooroooooooooooooooooooooooooooo", )
        startService(intent)
        println("thissssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss")

    }

    companion object {
        var PERMISSION_REQUEST_ACCESS_LOCATION=100;
    }

    fun checkPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true

        }
        else{
            return false
        }

    }
    fun isLocationEnabled():Boolean{
        var locationManager:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode== PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Permission Granted", Toast.LENGTH_LONG).show();
                getCurrentLocation()
            }
            else{
                Toast.makeText(applicationContext,"Permission Denied", Toast.LENGTH_LONG).show();

            }
        }
    }


//    override fun onDestroy(){
//        super.onDestroy()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
//
//    }
}