package com.mobdeve.finalproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
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
import com.mobdeve.finalproject.databinding.ActivityMapsMainBinding

class MapsMainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsMainBinding

    private lateinit var slideMenuSearch: View
    private lateinit var slideMenuSearchExpandButton: Button
    private lateinit var slideMenuEdit: View
    private lateinit var slideMenuEditExpandButton: Button
    private lateinit var destinationView: TextView
    private lateinit var distanceView: TextView

    private lateinit var marker: Marker

    private var slideMenuState: Int = 0 // 0 - down, 1 - up
    private var slideMenuDistance: Float = 0F

    private lateinit var chosenDestination: LatLng
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    private var hasTrip: Boolean = true
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

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

        /*
        // Set up location updater

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations){

                }
            }

        }*/

        // UI stuff

        binding.buttonSettings.setOnClickListener({
            var intent = Intent(applicationContext, SettingsActivity::class.java)
            this.startActivity(intent)
        })

        destinationView = binding.textViewDestination
        distanceView = binding.textViewETA

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

        binding.buttonSavedPlaces.setOnClickListener {
            var intent = Intent(applicationContext, SavedPlacesActivity::class.java)
            this.startActivityForResult(intent, 1)
        }

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

    }

    override fun onStart() {
        super.onStart()

        var status = this.intent.getStringExtra("STATUS")

        if(status == "no_destination") {
            slideMenuSearch.visibility = View.VISIBLE
            slideMenuEdit.visibility = View.GONE
            hasTrip = false
        }
        else if(status == "has_destination") {
            slideMenuEdit.visibility = View.VISIBLE
            slideMenuSearch.visibility = View.GONE
            hasTrip = true
        }

        // for testing
        hasTrip = true
        slideMenuEdit.visibility = View.VISIBLE
        slideMenuSearch.visibility = View.GONE
        slideMenuEdit.translationY = 0F
        slideMenuState = 1

        destinationView.setText("DLSU")
        val startPoint = Location("location1")
        startPoint.latitude = 14.5638
        startPoint.longitude = 120.9932
        val endPoint = Location("location2")
        endPoint.latitude = 14.554745
        endPoint.longitude = 120.997897
        val distanceB = endPoint.distanceTo(startPoint)
        distanceView.setText("Current distance: ${distanceB}")

    }
    /*
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause(){
        super.onPause()
        stopLocationUpdates()
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            1 -> {
                //get extras from intent
                //make latlng obj
                //put in chosen location
                //move cam and marker to chosen location
                val latitude : Float = data?.getFloatExtra("latitude", 0.00f) ?: 0.00f
                val longitude : Float = data?.getFloatExtra("longitude", 0.00f) ?: 0.00f
                chosenDestination = LatLng(latitude.toDouble(), longitude.toDouble())
                mMap.moveCamera(CameraUpdateFactory.newLatLng(chosenDestination))
                marker?.position = chosenDestination
            }
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

        // Add a marker in DLSU and move the camera
        val dlsu = LatLng(14.5638, 120.9932)
        marker = mMap.addMarker(MarkerOptions().position(dlsu))!!
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dlsu, 15.0f))

        mMap.setOnMapClickListener { latLng ->
            Log.d("STATE", "OnMapClickListener fired! latlng: ${latLng}")
            chosenDestination = latLng
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            marker?.position = latLng
        }

        autocompleteFragment.setOnPlaceSelectedListener(object :
            com.google.android.libraries.places.widget.listener.PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.d("STATE", "OnPlaceSelected fired! place.latlng: ${place.latLng}")
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

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun requestLocationPermissions(){
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.d("PERMISSIONS", "ONLY COARSE LOCATION PERMISSION GRANTED")
                    this.finish()
                    System.exit(0)
                } else -> {
                Log.d("PERMISSIONS", "NO PERMISSION GRANTED")
                this.finish()
                System.exit(0)
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    /*
    @RequiresApi(Build.VERSION_CODES.S)
    private fun startLocationUpdates(){

        // Check if location permissions is granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // if not, ask for location permissions

        }

        locationRequest = LocationRequest.Builder(1000)
            .setQuality(LocationRequest.QUALITY_HIGH_ACCURACY)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private fun stopLocationUpdates(){
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }*/

    private fun onApproachDestination(){
        
    }
}