package com.mobdeve.finalproject

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_places)

        val places = ArrayList<String>()
        places.add("Home")
        places.add("Work")
        places.add("School")
        places.add("Mom's House")

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


