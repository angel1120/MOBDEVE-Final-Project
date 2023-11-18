package com.mobdeve.finalproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.finalproject.databinding.ActivityStartBinding

class StartActivity: AppCompatActivity() {

    private lateinit var viewBinding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        viewBinding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnGo.setOnClickListener({
            val intent = Intent(applicationContext, MapsMainActivity::class.java)
            intent.putExtra("STATUS", "no_destination")
            this.startActivity(intent)
        })

        viewBinding.btnSettings.setOnClickListener({
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            this.startActivity(intent)
        })

    }

}