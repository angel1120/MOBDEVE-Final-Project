package com.mobdeve.finalproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.widget.SeekBar
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.mobdeve.finalproject.databinding.SettingsBinding
import java.io.IOException


class SettingsActivity: AppCompatActivity() {

    lateinit var proximity: Switch
    lateinit var vibration: Switch

    lateinit var audioManager : AudioManager

    val PICK_FILE = 99

    val TAG = "com.mobdeve.finalproject.sharedprefs"
    private val SEEK_BAR_PROGRESS_KEY = "seekBarProgress"
    private val AUDIO_NAME_KEY = "audioName"
    private val AUDIO_URI_KEY = "audioUri"
    private val VIBRATION_KEY = "vibrationSwitch"
    private val DISTANCE_VAL_KEY = "distanceValue"
    private val PROXIMITY_KEY = "proximitySwitch"

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var viewBinding: SettingsBinding
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = SettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        sharedPreferences = getSharedPreferences(TAG, Context.MODE_PRIVATE)

        val maxVolume: Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume: Int = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        val seekBar: SeekBar = findViewById(R.id.seekBar)
        seekBar.max = maxVolume
        seekBar.progress = currentVolume

        val savedProgress = sharedPreferences.getInt(SEEK_BAR_PROGRESS_KEY, 0)
        seekBar.progress = savedProgress

        val savedAudio = sharedPreferences.getString(AUDIO_NAME_KEY, "0")
        viewBinding.tvFilename.text = savedAudio

        val savedDistance = sharedPreferences.getString(DISTANCE_VAL_KEY, "50")
        viewBinding.etDistanceVal.setText(savedDistance)

        vibration = findViewById(R.id.swVibrate)
        val savedVibration = sharedPreferences.getBoolean(VIBRATION_KEY, false)
        vibration.isChecked = savedVibration

        proximity = findViewById(R.id.swProximity)
        val savedProximity = sharedPreferences.getBoolean(PROXIMITY_KEY, false)
        proximity.isChecked = savedProximity

        val editor = sharedPreferences.edit()

        vibration.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(VIBRATION_KEY, isChecked)
            editor.apply()

        }

        proximity.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(PROXIMITY_KEY, isChecked)
            editor.apply()

        }

        viewBinding.btnChooseFile.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "audio/*"
            startActivityForResult(intent, PICK_FILE)

        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Handle progress change
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                editor.putInt(SEEK_BAR_PROGRESS_KEY, progress)
                editor.apply()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle start tracking touch
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handle stop tracking touch
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Create the intent
        val intent = Intent(this, StartActivity::class.java)
        intent.putExtra("EXTRA_AUDIO", sharedPreferences.getString(AUDIO_URI_KEY, "0"))

        var distanceValue = viewBinding.etDistanceVal.text.toString()
        intent.putExtra("EXTRA_DISTANCE", distanceValue)

        intent.putExtra("EXTRA_VIBRATION", vibration.isChecked)
        intent.putExtra("EXTRA_PROXIMITY", proximity.isChecked)

        val editor = sharedPreferences.edit()
        editor.putString(DISTANCE_VAL_KEY, distanceValue)
        editor.apply()

        startActivity(intent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                val uri = data.data

                createMediaPlayer(uri)

            }
        }
    }

    fun createMediaPlayer(uri: Uri?) {
        try {
            val editor = sharedPreferences.edit()
            editor.putString(AUDIO_URI_KEY, uri.toString())
            editor.apply()
            viewBinding.tvFilename.setText(getNameFromUri(uri))

        } catch (e: IOException) {
            viewBinding.tvFilename.setText(e.toString())
        }
    }

    @SuppressLint("Range")
    fun getNameFromUri(uri: Uri?): String? {
        var fileName = ""
        var cursor: Cursor? = null

        if (uri != null) {
            cursor = contentResolver.query(
                uri, arrayOf(
                    MediaStore.Images.ImageColumns.DISPLAY_NAME
                ), null, null, null
            )

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME))

                    val editor = sharedPreferences.edit()
                    editor.putString(AUDIO_NAME_KEY, fileName)
                    editor.apply()
                }
            } finally {
                cursor?.close()
            }
        }

        return fileName
    }


}