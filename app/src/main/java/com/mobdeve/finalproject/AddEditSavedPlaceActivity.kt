package com.mobdeve.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
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
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.mobdeve.finalproject.databinding.AddEditSavedPlaceBinding

class AddEditSavedPlaceActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var viewBinding: AddEditSavedPlaceBinding
    private var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private lateinit var label_input: EditText

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        viewBinding = AddEditSavedPlaceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        label_input = findViewById(R.id.etLabel)

        viewBinding.btnApply.setOnClickListener {
            val myDB = DatabaseHandler(this@AddEditSavedPlaceActivity)
            myDB.addSavedPlace(label_input.getText().toString())

            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        Places.initialize(applicationContext, getString(R.string.my_map_api_key))
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragement)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener {

            override fun onError(p0: Status) {
                Toast.makeText(this@AddEditSavedPlaceActivity, "Some Error in Search", Toast.LENGTH_SHORT).show()

            }

            override fun onPlaceSelected(place: Place) {
                val add = place.address
                val id = place.id
                val n = place.name
                val latLng = place.latLng!!
                val marker = addMarker(latLng)
                marker.title = "$add"
                marker.snippet = "$id"

                val address = findViewById<TextView>(R.id.tvAddress)
                address.text = add

                zoomOnMap(latLng)
            }


        })

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun zoomOnMap(latLng: LatLng){
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 12f) // -> amount of zoom level
        mGoogleMap?.animateCamera(newLatLngZoom)
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

    private fun addMarker(position: LatLng): Marker {
        //Add simple marker
        val marker = mGoogleMap?.addMarker(
            MarkerOptions()
            .position(position)
            .title("Marker")
        )

        return marker!!
    }

}