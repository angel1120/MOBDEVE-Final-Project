package com.mobdeve.finalproject

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.SearchView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.mobdeve.finalproject.databinding.ActivityMapsMainBinding

class MapsMainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsMainBinding
//    private lateinit var searchView: SearchView

    private lateinit var slideMenuSearch: View
    private lateinit var slideMenuSearchExpandButton: Button
    private lateinit var slideMenuEdit: View
    private lateinit var slideMenuEditExpandButton: Button

    private var slideMenuState: Int = 0 // 0 - down, 1 - up
    private var slideMenuDistance: Float = 0F

    private lateinit var chosenDestination: LatLng
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appInfo = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val bundle = appInfo.metaData
        Places.initialize(applicationContext, bundle.getString("com.google.android.geo.API_KEY"))

        binding = ActivityMapsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Setup AutocompleteSupportFragment for search

        autocompleteFragment = AutocompleteSupportFragment()
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteFragment.setHint("Search for a location")

        binding.buttonSettings.setOnClickListener({
            var intent = Intent(applicationContext, SettingsActivity::class.java)
            this.startActivity(intent)
        })

        // search slide menu
        slideMenuSearch = binding.subMenuSearch
        slideMenuDistance = slideMenuSearch.translationY
        slideMenuSearchExpandButton = binding.buttonExpand

        slideMenuSearchExpandButton.setOnClickListener({
            if(slideMenuState == 0){
                slideMenuSearch.animate().translationY(0F)
                slideMenuSearchExpandButton.text = "Collapse"
                slideMenuState = 1
            }
            else {
                slideMenuSearch.animate().translationY(slideMenuDistance)
                slideMenuSearchExpandButton.text = "Expand"
                slideMenuState = 0
            }
        })

        binding.buttonSavedPlaces.setOnClickListener({
            var intent = Intent(applicationContext,SavedPlacesActivity::class.java)
            this.startActivity(intent)
        })

        binding.buttonApply.setOnClickListener({
            slideMenuSearch.animate().translationY(slideMenuDistance)
            slideMenuSearchExpandButton.text = "Expand"
            slideMenuState = 0
            slideMenuSearch.visibility = View.GONE
            slideMenuEdit.visibility = View.VISIBLE
        })

        // edit slide menu
        slideMenuEdit = binding.subMenuEdit
        slideMenuDistance = slideMenuEdit.translationY
        slideMenuEditExpandButton = binding.buttonExpand2

        slideMenuEditExpandButton.setOnClickListener({
            if(slideMenuState == 0){
                slideMenuEdit.animate().translationY(0F)
                slideMenuEditExpandButton.text = "Collapse"
                slideMenuState = 1
            }
            else {
                slideMenuEdit.animate().translationY(slideMenuDistance)
                slideMenuEditExpandButton.text = "Expand"
                slideMenuState = 0
            }
        })

        binding.buttonEditDestination.setOnClickListener({
            var intent = Intent(applicationContext, EditDestinationActivity::class.java)
            this.startActivity(intent)
        })

        var status = this.intent.getStringExtra("STATUS")

        if(status == "no_destination") {
            slideMenuSearch.visibility = View.VISIBLE
            slideMenuEdit.visibility = View.GONE
        }
        else if(status == "has_destination") {
            slideMenuEdit.visibility = View.VISIBLE
            slideMenuSearch.visibility = View.GONE
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val dlsu = LatLng(14.5638, 120.9932)
        val marker = mMap.addMarker(MarkerOptions().position(dlsu))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dlsu, 15.0f))

        mMap.setOnMapClickListener { latLng ->
            chosenDestination = latLng
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            marker?.position = latLng
        }

        autocompleteFragment.setOnPlaceSelectedListener(object :
            com.google.android.libraries.places.widget.listener.PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Handle the selected place
                val location = place.latLng
                marker?.position = location
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                chosenDestination = location
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                // Handle the error
            }
        })

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, autocompleteFragment)
            .commit()
//        searchView = findViewById(R.id.searchView)
//        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // Handle search submission if needed
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText.isNullOrBlank()) {
//                    autocompleteFragment.view?.visibility = android.view.View.VISIBLE
//                } else {
//                    autocompleteFragment.view?.visibility = android.view.View.GONE
//                }
//                return true
//            }
//        })
    }
}