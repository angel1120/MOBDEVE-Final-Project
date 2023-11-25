package com.mobdeve.finalproject

import android.content.Intent
<<<<<<< Updated upstream
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
=======
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
>>>>>>> Stashed changes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
<<<<<<< Updated upstream
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

import com.mobdeve.finalproject.databinding.AddEditSavedPlaceBinding

class AddEditSavedPlaceActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var viewBinding: AddEditSavedPlaceBinding
    private var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private lateinit var label_input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = AddEditSavedPlaceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        label_input = findViewById(R.id.etLabel)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val appInfo = applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        )
        val bundle = appInfo.metaData
        Places.initialize(applicationContext, bundle.getString("com.google.android.geo.API_KEY"))


        autocompleteFragment = AutocompleteSupportFragment()
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setHint("Search for a location")


        //grab String extra from intent saying whether we're updating or adding
        //  if adding, set map onclick listener and let user choose destination + label
        //      if no label auto "untitled"
        //      if no location auto dlsu
        //      if input is complete add le stuff by calling addPlace from adapter
        //  if updating, still set map onclick listener but marker and chosendestination var must be instantiated alr based on passed intent data
        //  label must also be instantiated based on intent data
        //      last value of chosendestination will be the updated thing
        //      grab whatevers in the editText box and use that too
        //      call updatePlace from adapter
        //  finish()
        // PS. latitude and longitude can both be taken from latLng object by literally just going place.latitude (where place is ur latLng obj)
        // PPS. copy the stuff in onMapReady in MapsMainActivity to make map move when user touch a place and to give search bar functionality, should be easy to read



    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        //Add Marker
        //    addMarker(LatLng(13.234, 12.543))

        //Add draggable marker (maybe if i wanna move the pins)
        /*    mGoogleMap?.addMarker(MarkerOptions()
                .position(LatLng(13.234, 12.543))
                .title("Draggable Marker")
                .draggable(true)
            ) */

        //Add custom Marker
        /*     mGoogleMap?.addMarker(MarkerOptions()
                 title
             ) */


    }
}
