package com.mobdeve.finalproject

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.finalproject.databinding.SettingsBinding

class SettingsActivity: AppCompatActivity() {

    private lateinit var viewBinding: SettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = SettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}