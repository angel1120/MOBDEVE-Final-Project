package com.mobdeve.finalproject

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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

class EditSavedPlaceActivity: AppCompatActivity(),  OnMapReadyCallback{

    private var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    private lateinit var editLabel: EditText
    private lateinit var tvAddressEdit: TextView
    private lateinit var tvNameEdit: TextView
    private lateinit var btnSaveEdit: Button
    private lateinit var btnBack: ImageButton

    var id: Int = 0
    lateinit var label: String
    lateinit var name: String
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_saved_place)

        this.id = intent.getStringExtra("id")?.toIntOrNull() ?: 0
        this.label = intent.getStringExtra("label").toString()
        this.name = intent.getStringExtra("name").toString()
        this.longitude = intent.getStringExtra("longitude")?.toDoubleOrNull() ?: 0.0
        this.latitude = intent.getStringExtra("latitude")?.toDoubleOrNull() ?: 0.0

        editLabel = findViewById(R.id.etLabelEdit)
        tvAddressEdit = findViewById(R.id.tvAddressEdit)
        tvNameEdit = findViewById(R.id.tvNameEdit)
        btnSaveEdit = findViewById(R.id.btnSaveEdit)
        btnBack = findViewById(R.id.ibBackEdit)

        editLabel.setText(label)
        tvAddressEdit.text = name
        tvNameEdit.text = name

        btnSaveEdit.setOnClickListener{
            val myDB = PlacesDatabase(this@EditSavedPlaceActivity)
            val updateSavedPlace = SavedPlaceItem(id, editLabel.text.toString(), name, latitude.toFloat(), longitude.toFloat())
            myDB.updatePlace(updateSavedPlace)

            finish()
        }

       btnBack.setOnClickListener{
            finish()
        }

        val appInfo = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val bundle = appInfo.metaData
        Places.initialize(applicationContext, bundle.getString("com.google.android.geo.API_KEY"))
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragementEdit)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener {

            override fun onError(p0: Status) {
                Toast.makeText(this@EditSavedPlaceActivity, "Some Error in Search", Toast.LENGTH_SHORT).show()

            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng!!
                addMarker(latLng)

                latitude = place.latLng.latitude
                longitude = place.latLng.longitude
                name = place.name

                val tvAddressEdit = findViewById<TextView>(R.id.tvAddressEdit)
                val tvNameEdit = findViewById<TextView>(R.id.tvNameEdit)

                tvAddressEdit.text = place.address
                tvNameEdit.text = name

                zoomOnMap(latLng)
            }


        })

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragmentEdit) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun zoomOnMap(latLng: LatLng){
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 12f) // -> amount of zoom level
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        this.id = intent.getStringExtra("id")?.toIntOrNull() ?: 0
        this.label = intent.getStringExtra("label").toString()
        this.name = intent.getStringExtra("name").toString()
        this.longitude = intent.getStringExtra("longitude")?.toDoubleOrNull() ?: 0.0
        this.latitude = intent.getStringExtra("latitude")?.toDoubleOrNull() ?: 0.0

        val saved = LatLng(latitude, longitude)
        mGoogleMap!!.addMarker(MarkerOptions().position(saved).title(name))
        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLng(saved))

        zoomOnMap(saved)

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