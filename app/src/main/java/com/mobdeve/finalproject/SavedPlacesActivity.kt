package com.mobdeve.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.finalproject.databinding.SavedPlacesBinding

class SavedPlacesActivity : AppCompatActivity() {

    private lateinit var viewBinding: SavedPlacesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = SavedPlacesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val places = ArrayList<String>()
        places.add("Home")
        places.add("Work")
        places.add("School")
        places.add("Mom's House")

        viewBinding.fabAddPlace.setOnClickListener({
            var intent = Intent(applicationContext, AddEditSavedPlaceActivity::class.java)
            this.startActivity(intent)
        })

        val recyclerView: RecyclerView = findViewById(R.id.rvSavedPlaces)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = savedPlaces_Adapter(this, places)
        recyclerView.adapter = adapter
    }

    fun showPopup(v: View){
        val popup = PopupMenu(v.context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.actions, popup.menu)
        popup.show()
    }
}


