package com.mobdeve.finalproject

import android.content.pm.PackageManager
import android.os.Bundle
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
import com.mobdeve.finalproject.databinding.AddSavedPlaceBinding

class AddSavedPlaceActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var viewBinding: AddSavedPlaceBinding
    private var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    lateinit var name: String
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        viewBinding = AddSavedPlaceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnApply.setOnClickListener {
            val myDB = PlacesDatabase(this@AddSavedPlaceActivity)
            val addSavedPlace = SavedPlaceItem(0, viewBinding.etLabel.text.toString(), name, latitude.toFloat(), longitude.toFloat())
            myDB.addPlace(addSavedPlace)

            finish()
        }

        viewBinding.ibBackAdd.setOnClickListener{
            finish()
        }

        val appInfo = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val bundle = appInfo.metaData
        Places.initialize(applicationContext, bundle.getString("com.google.android.geo.API_KEY"))
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragement)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener {

            override fun onError(p0: Status) {
                Toast.makeText(this@AddSavedPlaceActivity, "Some Error in Search", Toast.LENGTH_SHORT).show()

            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng!!
                addMarker(latLng)

                latitude = place.latLng.latitude
                longitude = place.latLng.longitude
                name = place.name

                viewBinding.tvAddress.text = place.address
                viewBinding.tvName.text = place.name

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