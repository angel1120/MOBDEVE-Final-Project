package com.mobdeve.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PlaceDatabase";
    public static final String PLACE_TABLE = "place_table";

    public static final String PLACE_ID = "id";
    public static final String PLACE_NAME = "place_name";
    public static final String PLACE_LAT = "place_lat";
    public static final String PLACE_LONG = "place_long";
    public Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PLACE_TABLE = "CREATE TABLE " + PLACE_TABLE +
                " (" + PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLACE_NAME + " TEXT, " +
                PLACE_LAT + " REAL, " +
                PLACE_LONG + " REAL);";
        sqLiteDatabase.execSQL(CREATE_PLACE_TABLE);

        SQLiteDatabase db = sqLiteDatabase;
        ContentValues cv1= new ContentValues();
        ContentValues cv2= new ContentValues();
        ContentValues cv3= new ContentValues();
        ContentValues cv4= new ContentValues();

        cv1.put(PLACE_NAME, "Vito Cruz");
        cv1.put(PLACE_LAT, 14.5634);
        cv1.put(PLACE_LONG, 120.9948);
        db.insert(PLACE_TABLE, null, cv1);

        cv2.put(PLACE_NAME, "Doroteo Jose");
        cv2.put(PLACE_LAT, 14.6056);
        cv2.put(PLACE_LONG, 120.9820);
        db.insert(PLACE_TABLE, null, cv2);

        cv3.put(PLACE_NAME, "Tayuman");
        cv3.put(PLACE_LAT, 14.6166);
        cv3.put(PLACE_LONG, 120.9827);
        db.insert(PLACE_TABLE, null, cv3);

        cv4.put(PLACE_NAME, "Balintawak");
        cv4.put(PLACE_LAT, 14.6577);
        cv4.put(PLACE_LONG, 121.0040);
        db.insert(PLACE_TABLE, null, cv4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PLACE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
