package com.mobdeve.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.Place
import com.mobdeve.finalproject.databinding.SavedPlacesBinding
import kotlin.properties.Delegates

class SavedPlacesActivity : AppCompatActivity(), savedPlaces_Adapter.OnItemClickListener {

    private lateinit var viewBinding: SavedPlacesBinding
    private var latitude: Float = 0.0f
    private var longitude: Float = 0.0f

    override fun onItemClick(latitude: Float, longitude: Float) {
        this.latitude = latitude
        this.longitude = longitude
        var result: Intent = Intent()
        result.putExtra("latitude", this.latitude)
        result.putExtra("longitude", this.longitude)
        setResult(1, result)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = SavedPlacesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.fabAddPlace.setOnClickListener({
            var intent = Intent(this, AddSavedPlaceActivity::class.java)
            this.startActivity(intent)
        })

        val recyclerView: RecyclerView = findViewById(R.id.rvSavedPlaces)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val placesDatabase: PlacesDatabase = PlacesDatabase(this)
        val adapter = savedPlaces_Adapter(placesDatabase.place, this, this)
        recyclerView.adapter = adapter

    }

}


