package com.mobdeve.finalproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.finalproject.databinding.AddEditSavedPlaceBinding

class AddEditSavedPlaceActivity: AppCompatActivity() {

    private lateinit var viewBinding: AddEditSavedPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        viewBinding = AddEditSavedPlaceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnApply.setOnClickListener({
            finish()
        })
    }

}