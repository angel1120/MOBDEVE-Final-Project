package com.mobdeve.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PlacesDatabase {

    private DatabaseHandler databaseHandler;

    public PlacesDatabase(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
    }
    public int addPlace(SavedPlaceItem place) {
        SQLiteDatabase db = databaseHandler.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.PLACE_NAME, place.getPlaceName());
        contentValues.put(DatabaseHandler.PLACE_LAT, place.getLatitude());
        contentValues.put(DatabaseHandler.PLACE_LONG, place.getLongitude());

        int _id = (int) db.insert(DatabaseHandler.PLACE_TABLE, null, contentValues);

        db.close();

        return _id;
    }

    public void updatePlace(SavedPlaceItem place) {

        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHandler.PLACE_NAME, place.getPlaceName());
        contentValues.put(DatabaseHandler.PLACE_LAT, place.getLatitude());
        contentValues.put(DatabaseHandler.PLACE_LONG, place.getLongitude());
        String id = String.valueOf(place.getPlaceID());
        db.update(databaseHandler.PLACE_TABLE, contentValues, "id=?", new String[]{id});
    }


    public void deletePlace(SavedPlaceItem place) {
        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        String id = String.valueOf(place.getPlaceID());
        db.delete(DatabaseHandler.PLACE_TABLE, "id=?", new String[]{id});

    }

    public ArrayList<SavedPlaceItem> getPlace() {
        ArrayList<SavedPlaceItem> result = new ArrayList<SavedPlaceItem>();

        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHandler.PLACE_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        int id;
        String name;
        float latitude;
        float longitude;

        while(cursor.moveToNext()){
            id = cursor.getInt(0);
            name = cursor.getString(1);
            latitude = cursor.getFloat(2);
            longitude = cursor.getFloat(3);

            SavedPlaceItem placeItem = new SavedPlaceItem(id, name, latitude, longitude);

            result.add(placeItem);
        }


        return result;
    }
}
