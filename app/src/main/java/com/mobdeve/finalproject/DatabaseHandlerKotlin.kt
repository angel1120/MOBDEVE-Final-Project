package com.mobdeve.finalproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


class DatabaseHandlerKotlin(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val context: Context? = context

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "SavedPlacesDatabase.db" // Database name
        private const val TABLE_NAME = "saved_places" // Table Name

        //All the Columns names
        private const val COLUMN_ID = "_id"
        private const val COLUMN_LABEL = "label"
    //    private const val COLUMN_LATITUDE = "latitude"
    //    private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val query = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_LABEL + " TEXT)")

        db?.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addSavedPlace(label:String){
        val db: SQLiteDatabase = writableDatabase

        val contentValues = ContentValues()

        contentValues.put(COLUMN_LABEL, label)
        val result: Long = db.insert(TABLE_NAME, null, contentValues)
        if (result == -1L) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show()
        }


    }

    fun readAllData(): Cursor? {
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun updateData(row_id: String, label: String?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_LABEL, label)
        val result = db.update(TABLE_NAME, cv, "_id=?", arrayOf<String>(row_id)).toLong()
        if (result == -1L) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show()
        }
    }

}