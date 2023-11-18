package com.mobdeve.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mobdeve.finalproject.databinding.ActivityMapsMainBinding

class MapsMainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsMainBinding

    private lateinit var slideMenuSearch: View
    private lateinit var slideMenuSearchExpandButton: Button
    private lateinit var slideMenuEdit: View
    private lateinit var slideMenuEditExpandButton: Button

    private var slideMenuState: Int = 0 // 0 - down, 1 - up
    private var slideMenuDistance: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


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
        mMap.addMarker(MarkerOptions().position(dlsu).title("DLSU"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dlsu, 15.0f))
    }
}