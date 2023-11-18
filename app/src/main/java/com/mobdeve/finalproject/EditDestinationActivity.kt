package com.mobdeve.finalproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.finalproject.databinding.EditDestinationBinding

class EditDestinationActivity: AppCompatActivity() {

    private lateinit var viewBinding: EditDestinationBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        viewBinding = EditDestinationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnApply.setOnClickListener({
            var intent = Intent(applicationContext, MapsMainActivity::class.java)
            intent.putExtra("STATUS", "has_destination")
            // to-do: Make sure the same activity is not launched multiple times
            this.startActivity(intent)
        })

        viewBinding.btnCancel.setOnClickListener({
            var intent = Intent(applicationContext, MapsMainActivity::class.java)
            intent.putExtra("STATUS", "no_destination")
            // to-do: Make sure the same activity is not launched multiple times
            this.startActivity(intent)
        })
    }

}